package com.edu.neu.project.dao;


import com.edu.neu.project.entity.RememberMe;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository

public class RememberMeDAO extends AbstractHibernateDao<RememberMe> implements PersistentTokenRepository {

    public RememberMe findByUsername(String username) {
        Session session = this.getCurrentSession();
        Criteria crit = session.createCriteria(RememberMe.class);
        crit.add(Restrictions.eq("USERNAME", username));
        return (RememberMe) crit.uniqueResult();
    }

    public RememberMe findBySeries(String series) {
        return this.getCurrentSession().find(RememberMe.class, series);
    }

    public void deleteByUsername(String userName) {
        this.delete(this.findByUsername(userName));
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        RememberMe rm = new RememberMe()
                .setSeries(token.getSeries())
                .setUsername(token.getUsername())
                .setToken(token.getTokenValue())
                .setLastUsed(token.getDate());
        this.create(rm);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        RememberMe rememberMe = this.findBySeries(series);
        rememberMe.setToken(tokenValue);
        rememberMe.setLastUsed(lastUsed);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        RememberMe rememberMe = this.findBySeries(seriesId);
        if (rememberMe != null) {
            return new PersistentRememberMeToken(rememberMe.getUsername(),
                    rememberMe.getSeries(), rememberMe.getToken(), rememberMe.getLastUsed());
        }
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        this.deleteByUsername(username);
    }
}
