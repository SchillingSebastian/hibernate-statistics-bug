import bugs.Shop
import bugs.ShopType
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import jakarta.persistence.criteria.Root
import org.hibernate.Session
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class Bug {

    private lateinit var entityManagerFactory: EntityManagerFactory

    @BeforeEach
    fun init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("templatePU")
    }

    @AfterEach
    fun destroy() {
        entityManagerFactory.close()
    }

    @Test
    fun `WHEN counting statistics THEN they are correct`(){
        setup()
        val entityManager = entityManagerFactory.createEntityManager()
        val statistics = entityManager.unwrap(Session::class.java).sessionFactory.statistics
        statistics.isStatisticsEnabled = true
        statistics.clear()
        val criteriaBuilder = entityManager.criteriaBuilder
        entityManager.transaction.begin()
        val query = criteriaBuilder.createQuery(Shop::class.java)
        val root:Root<Shop> = query.from(Shop::class.java)
        entityManager.createQuery(query.select(root)).resultList
        entityManager.transaction.commit()
        // closing the em calls StatisticalLoggingSessionEventListener.end() and logs the
        // jdbc stats which are correct
        entityManager.close()
        // 1 select * from shop
        // 1 select shopType1.id from ShopType
        // 1 select shopType2.id from ShopType
        assert(statistics.queryExecutionCount == 3L) // fails, is 1
    }


    /**
     * This tests creates two Shops with the same ShopType
     */
    private fun setup(): String {
        val entityManager = entityManagerFactory.createEntityManager()
        entityManager.transaction.begin()
        val franchiseType = "HIBERNATE"
        val shopType1 = ShopType()
        entityManager.persist(shopType1)
        val shopType2 = ShopType()
        entityManager.persist(shopType2)
        entityManager.persist(
            Shop(shopType = shopType1)
        )
        entityManager.persist(
            Shop(shopType = shopType2)
        )
        entityManager.transaction.commit()
        entityManager.close()
        return franchiseType
    }
}