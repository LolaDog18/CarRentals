package com.woozy.carrentals.service;

import com.woozy.carrentals.exceptions.CustomerEntityException;
import com.woozy.carrentals.io.entity.CustomerEntity;
import com.woozy.carrentals.repository.CustomerRepository;
import com.woozy.carrentals.service.impl.CustomerServiceImpl;
import com.woozy.carrentals.shared.dto.request.customer.UpdateCustomerRequestDto;
import com.woozy.carrentals.utils.BeanHelperUtils;
import com.woozy.carrentals.utils.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static com.woozy.carrentals.exceptions.errormessages.ServiceErrMsg.CUSTOMER_ID_NOT_FOUND;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTests {
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;
    private CustomerEntity customerEntity;
    private String userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();
    }

    @Test
    void getCustomer_validUserId_returnsCustomerResponse() {
        customerEntity = TestDataGenerator.generateCustomerEntity();
        customerEntity.setUserId(userId);

        when(customerRepository.findByUserId(userId)).thenReturn(Optional.of(customerEntity));

        var result = customerService.getCustomer(userId);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    @Test
    void getCustomer_invalidUserId_throwsCustomerEntityException() {
        when(customerRepository.findByUserId(userId)).thenReturn(Optional.empty());

        CustomerEntityException exception = assertThrows(CustomerEntityException.class, () -> customerService.getCustomer(userId));
        assertEquals(format(CUSTOMER_ID_NOT_FOUND, userId), exception.getMessage());
    }

    @Test
    void updateCustomer_validUserId_returnsCustomerResponse() {
        customerEntity = TestDataGenerator.generateCustomerEntity();
        customerEntity.setUserId(userId);

        when(customerRepository.findByUserId(userId)).thenReturn(Optional.of(customerEntity));
        var updateCustomerRequest = TestDataGenerator.generateUpdateCustomerRequestDto();

        var result = customerService.updateCustomer(userId, updateCustomerRequest);

        assertNotNull(result);
        assertAll(
                () -> assertEquals(userId, result.getUserId()),
                () -> assertEquals(updateCustomerRequest.getEmail(), result.getEmail()),
                () -> assertEquals(updateCustomerRequest.getMobileNumber(), result.getMobileNumber()),
                () -> assertEquals(updateCustomerRequest.getAddress(), result.getAddress())
        );
        verify(customerRepository, times(1)).save(customerEntity);
    }

    @Test
    void updateCustomer_invalidUserId_throwsUsernameNotFoundException() {
        when(customerRepository.findByUserId(userId)).thenReturn(Optional.empty());

        var updateCustomerRequest = TestDataGenerator.generateUpdateCustomerRequestDto();

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> customerService.updateCustomer(userId, updateCustomerRequest));

        assertEquals(format(CUSTOMER_ID_NOT_FOUND, userId), exception.getMessage());
        verify(customerRepository, never()).save(any(CustomerEntity.class));
    }

    @Test
    void updateCustomer_requestFieldsNull_theseResponseFieldsNonNull() {
        var customerEntity = TestDataGenerator.generateCustomerEntity();
        customerEntity.setUserId(userId);
        var updateCustomerRequest = new UpdateCustomerRequestDto();

        when(customerRepository.findByUserId(userId)).thenReturn(Optional.of(customerEntity));

        var result = customerService.updateCustomer(userId, updateCustomerRequest);

        assertFalse(BeanHelperUtils.areAllPropertiesNull(result));
        verify(customerRepository, never()).save(any(CustomerEntity.class));
    }

    @Test
    void deleteCustomer_validUserId_deletesCustomer() {
        customerEntity = TestDataGenerator.generateCustomerEntity();
        customerEntity.setUserId(userId);

        when(customerRepository.findByUserId(userId)).thenReturn(Optional.of(customerEntity));
        customerService.deleteCustomer(userId);
        verify(customerRepository, times(1)).delete(customerEntity);
    }

    @Test
    void deleteCustomer_invalidUserId_throwsUsernameNotFoundException() {
        when(customerRepository.findByUserId(userId)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> customerService.deleteCustomer(userId));
        assertEquals(format(CUSTOMER_ID_NOT_FOUND, userId), exception.getMessage());
        verify(customerRepository, never()).delete(any(CustomerEntity.class));
    }
}
