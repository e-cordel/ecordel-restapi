package br.com.itsmemario.ecordel.xilogravura;

import org.springframework.stereotype.Service;

@Service
public class XilogravuraService {

    private XilogravuraRepository xilogravuraRepository;

    public XilogravuraService(XilogravuraRepository xilogravuraRepository) {
        this.xilogravuraRepository = xilogravuraRepository;
    }

    public Xilogravura save(Xilogravura xilogravura){
        return xilogravuraRepository.save(xilogravura);
    }

}
