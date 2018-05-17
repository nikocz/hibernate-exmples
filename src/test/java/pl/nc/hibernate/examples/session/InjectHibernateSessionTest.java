package pl.nc.hibernate.examples.session;

import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.nc.hibernate.examples.common.TxTemplate;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InjectHibernateSessionTest {
  @Autowired
  private TxTemplate tx;
  @Autowired
  private EntityManager em;

  @Test
  public void willHaveAccessToHibernateSession() {
    tx.execute(() -> {
      //when
      final Session session = em.unwrap(Session.class);

      //then
      assertThat(session).isNotNull();
      assertThat(session.isOpen()).isTrue();
    });
  }

  @Test
  public void willHaveNoAccessToHibernateSessionOutsideTransaction() {
    //when
    final Throwable thrown = catchThrowable(() -> em.unwrap(Session.class));

    //then
    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }
}
