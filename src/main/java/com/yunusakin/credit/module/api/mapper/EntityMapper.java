package com.yunusakin.credit.module.api.mapper;

import com.yunusakin.credit.module.api.controller.dto.CustomerDTO;
import com.yunusakin.credit.module.api.controller.dto.LoanDTO;
import com.yunusakin.credit.module.api.controller.dto.LoanInstallmentDTO;
import com.yunusakin.credit.module.api.repository.domain.Customer;
import com.yunusakin.credit.module.api.repository.domain.Loan;
import com.yunusakin.credit.module.api.repository.domain.LoanInstallment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    Customer toCustomerEntity(CustomerDTO customerDTO);
    CustomerDTO toCustomerDTO(Customer customer);

    LoanDTO toLoanDTO(Loan loan);
    Loan toLoanEntity(LoanDTO loanDTO);

    LoanInstallment toInstalment(LoanInstallmentDTO loanInstallmentDTO);
    LoanInstallmentDTO toInstallmentDTO(LoanInstallment loanInstallmentDTO);
}
