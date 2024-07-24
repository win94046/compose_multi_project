package data.local

import domain.MongoRepository
import domain.model.Currency
import domain.model.RequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MongoImpl : MongoRepository {
    private var realm: Realm? = null

    init {
        configureTheRealm()
    }
    override fun configureTheRealm() {
        if (realm == null || realm?.isClosed() == true) {
            val config = RealmConfiguration.Builder(
                schema = setOf(
                    Currency::class
                )
            )
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    override suspend fun insertCurrencyData(currency: Currency) {

        realm?.write {

            copyToRealm(currency)
        }
    }

    override fun readCurrencyData(): Flow<RequestState<List<Currency>>> {
        println("HomeViewModel: readCurrencyData ")

        // 检查 realm 是否为 null
        val query = realm?.query<Currency>()
        println("HomeViewModel: Query result: $query")

        // 检查 asFlow 的结果
        val flowResult = query?.asFlow()
        println("HomeViewModel: Flow result: $flowResult")

        // 检查 map 操作
        val mappedFlow = flowResult?.map { result ->
            val dataList = result.list
            println("HomeViewModel: Mapping result: $dataList")
            RequestState.Success(data = dataList)
        }

        // 检查最终返回的 flow
        val finalFlow = mappedFlow ?: flow {RequestState.Error(message = "Realm not configured")}
        println("HomeViewModel: Final flow: $finalFlow")

        return finalFlow
    }


    override suspend fun cleanUp() {
        realm?.write {
            val currencies = this.query<Currency>().find()
            delete(currencies)
        }
    }
}