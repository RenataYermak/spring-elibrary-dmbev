package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.service.database.querydsl.QPredicates;
import org.example.service.database.repository.UserRepository;
import org.example.service.dto.userDto.UserCreateEditDto;
import org.example.service.dto.userDto.UserFilter;
import org.example.service.dto.userDto.UserReadDto;
import org.example.service.mapper.userMapper.UserCreateEditMapper;
import org.example.service.mapper.userMapper.UserReadMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.ResourceBundle.getBundle;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.example.service.database.entity.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;
    private final EmailService emailService;


    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        var predicate = QPredicates.builder()
                .add(filter.firstname(), user.firstname::containsIgnoreCase)
                .add(filter.lastname(), user.lastname::containsIgnoreCase)
                .add(filter.email(), user.email::containsIgnoreCase)
                .build();

        return userRepository.findAll(predicate, pageable)
                .map(userReadMapper::map);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::map)
                .toList();
    }


    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::map);
    }

    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) {
        UserReadDto userReadDto = Optional.of(userDto)
                .map(userCreateEditMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();

        if (!isEmpty(userDto.getEmail())) {
            String messageFromBundle = getBundle("messages", Locale.getDefault()).getString("email.message.registration");
            String subjectFromBundle = getBundle("messages", Locale.getDefault()).getString("email.subject.registration");
            String message = MessageFormat.format(messageFromBundle, userDto.getFirstname(), userDto.getEmail(), userDto.getRawPassword());
            emailService.sendMessage(userDto.getEmail(), subjectFromBundle, message);
        }
        return userReadDto;
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userDto) {
        return userRepository.findById(id)
                .map(entity -> userCreateEditMapper.map(userDto, entity))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user -> new CustomUser(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singleton(user.getRole()),
                        user.getId()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }
}
