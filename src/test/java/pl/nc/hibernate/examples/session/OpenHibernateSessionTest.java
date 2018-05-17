package pl.nc.hibernate.examples.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static pl.nc.hibernate.examples.common.CommonAssertions.assertPlanetChanged;
import static pl.nc.hibernate.examples.common.CommonOps.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class OpenHibernateSessionTest {
  @Autowired
  private EntityManager em;
  @Autowired
  private EntityManagerFactory emf;

  @Test
  public void willAllowToManuallyOpenSession() {
    //when
    final SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
    final Session session = sessionFactory.openSession();
    session.beginTransaction();
    updatePlanet(session, VENUS_ID);
    session.getTransaction().commit();
    session.close();

    //then
    assertPlanetChanged(em, VENUS_ID);
  }
}
