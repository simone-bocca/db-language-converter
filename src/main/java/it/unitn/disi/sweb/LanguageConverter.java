
package it.unitn.disi.sweb;

import it.unitn.disi.languages.utils.LanguageUtils;
import it.unitn.disi.sweb.core.common.utils.ContextLoader;
import it.unitn.disi.sweb.core.kb.IVocabularyService;
import it.unitn.disi.sweb.core.kb.model.vocabularies.Vocabulary;
import it.unitn.disi.sweb.core.common.repository.IGenericDao;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;


@Configuration

@ComponentScan("it.unitn.disi.sweb.core.kb")
@Component(value = "ILanguageConverter")

public class LanguageConverter implements ILanguageConverter{

    @Autowired
    IVocabularyService vocabularyService;
    @Autowired
    @Qualifier("vocabularyDao")
    IGenericDao genericDao;

    LanguageUtils langUtil;


    public static void main(String[] args) {

        ContextLoader loader = new ContextLoader();
        ApplicationContext context = loader.getApplicationContext();

        ILanguageConverter converter = (ILanguageConverter) context.getBean("ILanguageConverter");
        converter.process();
    }

    @Transactional
    public void process() {

        langUtil = new LanguageUtils();
        ArrayList<Vocabulary> vocabularies2Chars = (ArrayList<Vocabulary>) vocabularyService.readVocabularies(null);

        // Query examples
        //Query query = genericDao.createQuery("UPDATE Vocabulary v SET v.languageCode = 'en' WHERE v.languageCode = 'engl'");
        //Query query = genericDao.createQuery("SELECT v FROM Vocabulary v WHERE v.languageCode=:l");
        //Query query = genericDao.createQuery("DELETE Vocabulary v WHERE v.languageCode = 'eng'");

        for(Vocabulary voc: vocabularies2Chars) {
            Query query = genericDao.createQuery("UPDATE Vocabulary v SET v.languageCode =:c3 WHERE v.languageCode =:c2");
            query.setParameter("c2", voc.getLanguageCode());
            query.setParameter("c3", langUtil.getISO3Code(voc.getLanguageCode()));
            query.setCacheable(true);
            query.executeUpdate();
        }

        ArrayList<Vocabulary> vocabularies = (ArrayList<Vocabulary>) vocabularyService.readVocabularies(null);
        for (Vocabulary v: vocabularies){
            System.out.println(v.getLanguageCode());
        }
    }
}
