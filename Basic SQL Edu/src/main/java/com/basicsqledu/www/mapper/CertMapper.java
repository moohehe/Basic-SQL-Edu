package com.basicsqledu.www.mapper;

import com.basicsqledu.www.vo.Certification;

public interface CertMapper {

	public int insertCert(Certification certify);
	
	public Certification selectCert(Certification cert);

	public Certification searchCert(String temp_word);
}
