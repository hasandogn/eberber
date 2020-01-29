package com.demo.eberber.service;

import com.demo.eberber.domain.Staff;
import com.demo.eberber.domain.Barber;
import com.demo.eberber.exception.BadResourceException;
import com.demo.eberber.exception.ResourceAlreadyExistsException;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.repository.StaffRepository;
import com.demo.eberber.specification.StaffSpecification;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class StaffService {
    @Autowired
    private  StaffRepository staffRepository;

}
