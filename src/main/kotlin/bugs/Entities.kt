package bugs

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.*

@Entity
class Shop(
    @Id val id: UUID = UUID.randomUUID(),
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_type_id")
    var shopType: ShopType,
)

@Entity
class ShopType(
    @Id val id: UUID = UUID.randomUUID()
)