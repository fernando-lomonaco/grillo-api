package br.com.grillo.service.impl;

import br.com.grillo.model.entity.Finance;
import br.com.grillo.repository.FinanceRepository;
import br.com.grillo.service.FinanceService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    public static final String ACCOUNT_SID = "AC3432392372c5f273af787ab4160f8e63";
    public static final String AUTH_TOKEN = "0408007d133148899007706280c13839";

    private final FinanceRepository repository;

    @Override
    public Page<Finance> all(String product, String partner, Pageable pageable) {
        if (product.isEmpty() && partner.isEmpty()) {
            return this.findAll(pageable);
        }
        return this.findByProductCode(product, partner, pageable);
    }

    @Cacheable(value = "finances", key = "#pageable.getPageNumber().toString()", unless = "#result==null or #result.isEmpty()")
    public Page<Finance> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Cacheable(value = "financesByProductOrPartner", unless = "#result==null or #result.isEmpty()", key = "#product.concat('-').concat(#partner).concat('-').concat(#pageable.getPageNumber().toString())")
    public Page<Finance> findByProductCode(String product, String partner, Pageable pageable) {
        return repository.findByProductCode(UUID.fromString(product), pageable);
    }

    @Override
    @Cacheable(value = "financeId", key = "#code")
    public Optional<Finance> get(final String code) {
        return repository.findById(UUID.fromString(code));
    }

    @Override
    @CacheEvict(value = {"finances", "financesByProductOrPartner"}, allEntries = true)
    public Finance save(final Finance financeRequest) {
        Finance finance = repository.save(financeRequest);

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber("+5511995316886"), new PhoneNumber("+12674582631"),
                "Informações Financeira\n" + " Tipo de Finanças: " + finance.getFinanceType() + "\n Produto: " + finance.getProduct()).create();

        return finance;
    }

    @Override
    @Caching(evict = @CacheEvict(value = {"finances", "financesByProductOrPartner"}, allEntries = true), put = @CachePut(value = "financeId", key = "#code"))
    public Finance update(final Finance financeRequest, final String code) {
        financeRequest.setCode(UUID.fromString(code));
        return repository.save(financeRequest);
    }

    @Override
    @CacheEvict(value = {"finances", "financesByProductOrPartner", "financeId"}, allEntries = true)
    public void delete(final String code) {
        repository.deleteById(UUID.fromString(code));
    }

}
