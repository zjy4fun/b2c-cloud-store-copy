package org.example.service;

import org.example.pojo.Address;
import org.example.utils.R;

public interface AddressService {
    R list(Integer userId);

    R save(Address address);

    R remove(Integer addressId);
}
