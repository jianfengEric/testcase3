package com.gea.portal.ewp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gea.portal.ewp.dto.CountryDto;
import com.gea.portal.ewp.entity.Country;
import com.gea.portal.ewp.repository.CountryRepository;
import com.gea.portal.ewp.service.CountryService;

/**
 * Created by Dell on 2018/8/29.
 */
@Service
@Transactional
public class CountryServiceImpl implements CountryService{


    @Autowired
    private CountryRepository countryRepository;

	@Override
	public List<CountryDto> findAll() {
		List<Country> list = countryRepository.findAll();
		List<CountryDto> reslist = new ArrayList<>();
		for(Country item : list){
			CountryDto dto = new CountryDto();
			BeanUtils.copyProperties(item, dto);
			reslist.add(dto);
		}
		return reslist;
	}

    

}
