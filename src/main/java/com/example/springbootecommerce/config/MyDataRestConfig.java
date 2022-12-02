package com.example.springbootecommerce.config;

import com.example.springbootecommerce.entity.Product;
import com.example.springbootecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.config.RepositoryConfiguration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private final EntityManager entityManager;


    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager){
        entityManager= theEntityManager;
    }


    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
//        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
        HttpMethod[] theUnsupportedAccion ={HttpMethod.PUT,HttpMethod.POST,HttpMethod.DELETE};

        config.getExposureConfiguration().
                forDomainType(Product.class).
                withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedAccion)).
                withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedAccion));


        config.getExposureConfiguration().
                forDomainType(ProductCategory.class).
                withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedAccion)).
                withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedAccion));

        //call an internal helper hethod

        exposeIds(config);

    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // expose entity ids
        //

        // - get a list of all entity classes from the entity manager
        Set<javax.persistence.metamodel.EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // - create an array of the entity types
        List<Class> entityClasses = new ArrayList<>();

        // - get the entity types for the entities
        for (EntityType tempEntityType : entities) {
            entityClasses.add(tempEntityType.getJavaType());
        }

        // - expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }




    }

