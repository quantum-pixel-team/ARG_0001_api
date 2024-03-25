package com.quantum_pixel.arg.service;

import com.quantum_pixel.arg.user.repository.UserRepository;
import com.quantum_pixel.arg.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService sut;

    @Test
    void deleteUsersById() {
        //given
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        List<Long> userIdList = List.of(1L, 2L, 3L, 4L);
        //when
        sut.deleteUsersById(userIdList);
        //then
        verify(userRepository, times(userIdList.size())).deleteById(argumentCaptor.capture());
        assertThat(argumentCaptor.getAllValues()).isNotNull().containsAll(userIdList);

    }


}