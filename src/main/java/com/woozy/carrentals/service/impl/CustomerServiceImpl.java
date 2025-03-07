package com.woozy.carrentals.service.impl;

import com.woozy.UpdateCustomerRequestDto;
import com.woozy.carrentals.exceptions.CustomerEntityException;
import com.woozy.carrentals.io.entity.CustomerEntity;
import com.woozy.carrentals.repository.CustomerRepository;
import com.woozy.carrentals.service.CustomerService;
import com.woozy.carrentals.shared.dto.response.customer.CustomerResponseDto;
import com.woozy.carrentals.utils.BeanHelperUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.woozy.carrentals.exceptions.errormessages.ServiceErrMsg.CUSTOMER_ID_NOT_FOUND;
import static java.lang.String.format;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @SneakyThrows
    @Override
    public CustomerResponseDto updateCustomer(String userId, UpdateCustomerRequestDto user) {
        CustomerEntity foundUser = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(format(CUSTOMER_ID_NOT_FOUND, userId)));

        CustomerResponseDto customerResponseDto = new CustomerResponseDto();

        if (BeanHelperUtils.areAllPropertiesNull(user)) {
            log.info("Customer details are not provided for update");
            BeanHelperUtils.copyProperties(customerResponseDto, foundUser);
            return customerResponseDto;
        }

        BeanHelperUtils.copyProperties(foundUser, user);
        customerRepository.save(foundUser);
        BeanHelperUtils.copyProperties(customerResponseDto, foundUser);
        return customerResponseDto;
    }

    @Override
    public CustomerResponseDto getCustomer(String userId) {
        CustomerEntity user = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomerEntityException(format(CUSTOMER_ID_NOT_FOUND, userId)));

        CustomerResponseDto customerResponseDto = new CustomerResponseDto();
        org.springframework.beans.BeanUtils.copyProperties(user, customerResponseDto);
        return customerResponseDto;
    }

    @Override
    public void deleteCustomer(String userId) {
        CustomerEntity foundUser = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(format(CUSTOMER_ID_NOT_FOUND, userId)));

        customerRepository.delete(foundUser);
    }
}
