package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.local.dao.OrdenDao
import com.pasteleria_app.pasteleria_app.data.local.entities.OrdenEntity
import com.pasteleria_app.pasteleria_app.data.local.entities.OrdenItemEntity
import com.pasteleria_app.pasteleria_app.data.model.Pedido
import com.pasteleria_app.pasteleria_app.data.model.PedidoItem
import com.pasteleria_app.pasteleria_app.data.network.ApiService
import com.pasteleria_app.pasteleria_app.data.preferences.UserPreferences
import com.pasteleria_app.pasteleria_app.domain.model.Orden
import com.pasteleria_app.pasteleria_app.domain.model.OrdenItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class OrdenRepositoryImplTest {

    private lateinit var repository: OrdenRepositoryImpl
    private val dao: OrdenDao = mockk(relaxed = true)
    private val apiService: ApiService = mockk()
    private val userPreferences: UserPreferences = mockk()

    @Before
    fun setup() {
        repository = OrdenRepositoryImpl(dao, apiService, userPreferences)
    }

    @Test
    fun `crearOrden success`() = runTest {
        // Given
        val orden = Orden(
            id = "1",
            trackingId = "TRACK123",
            fechaCreacion = 1234567890L,
            estado = "PENDIENTE",
            items = listOf(
                OrdenItem(
                    productoId = 1L,
                    nombreProducto = "Pastel",
                    cantidad = 1,
                    precioUnitario = 1000,
                    subtotal = 1000,
                    mensaje = "msg"
                )
            ),
            total = 1000,
            tipoEntrega = "Despacho",
            direccionEntrega = "Calle 123",
            comuna = "Concepcion",
            fechaPreferida = "2023-12-01"
        )
        val userId = "user123"
        
        every { userPreferences.userIdFlow } returns flowOf(123L)
        coEvery { apiService.createPedido(any()) } returns Response.success(mockk())

        // When
        repository.crearOrden(orden, userId)

        // Then
        coVerify { apiService.createPedido(any()) }
        coVerify { dao.insertarOrdenCompleta(any(), any()) }
    }

    @Test(expected = Exception::class)
    fun `crearOrden failure API error`() = runTest {
        // Given
        val orden = Orden(
            id = "1",
            trackingId = "TRACK123",
            fechaCreacion = 1234567890L,
            estado = "PENDIENTE",
            items = emptyList(),
            total = 0,
            tipoEntrega = "Retiro",
            direccionEntrega = "",
            comuna = "",
            fechaPreferida = ""
        )
        every { userPreferences.userIdFlow } returns flowOf(123L)
        coEvery { apiService.createPedido(any()) } returns Response.error(500, "".toResponseBody())

        // When
        repository.crearOrden(orden, "user123")
    }

    @Test(expected = Exception::class)
    fun `crearOrden failure User ID missing`() = runTest {
        // Given
        val orden = Orden(
            id = "1",
            trackingId = "TRACK123",
            fechaCreacion = 1234567890L,
            estado = "PENDIENTE",
            items = emptyList(),
            total = 0,
            tipoEntrega = "Retiro",
            direccionEntrega = "",
            comuna = "",
            fechaPreferida = ""
        )
        every { userPreferences.userIdFlow } returns flowOf() // Empty flow

        // When
        repository.crearOrden(orden, "user123")
    }

    @Test
    fun `getOrdenes success`() = runTest {
        // Given
        val pedido = Pedido(
            id = 1,
            userId = 123,
            fecha = "2023-12-01T10:00:00",
            total = 1000.0,
            direccionEntrega = "Calle 123",
            regionNombre = "Biobío",
            comuna = "Concepcion",
            fechaEntrega = "2023-12-02",
            items = listOf(
                PedidoItem(
                    productoId = 1L,
                    productoNombre = "Pastel",
                    cantidad = 1,
                    precioUnitario = 1000.0,
                    subtotal = 1000.0,
                    mensaje = "msg"
                )
            ),
            estado = "PENDIENTE"
        )
        coEvery { apiService.getMisPedidos() } returns listOf(pedido)

        // When
        val result = repository.getOrdenes("user123").toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(1, result[0].size)
        assertEquals("1", result[0][0].id)
        assertEquals(1000, result[0][0].total)
    }

    @Test
    fun `getOrdenes empty list`() = runTest {
        // Given
        coEvery { apiService.getMisPedidos() } returns emptyList()

        // When
        val result = repository.getOrdenes("user123").toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0].isEmpty())
    }

    @Test
    fun `getOrdenes API error returns empty list`() = runTest {
        // Given
        coEvery { apiService.getMisPedidos() } throws Exception("Network error")

        // When
        val result = repository.getOrdenes("user123").toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0].isEmpty())
    }

    @Test
    fun `getAllOrdenes success`() = runTest {
        // Given
        val pedido = Pedido(
            id = 1,
            userId = 123,
            total = 2000.0,
            items = emptyList(),
            fechaEntrega = "2023-12-02",
            direccionEntrega = "Calle 123",
            regionNombre = "Biobío",
            comuna = "Concepcion"
        )
        coEvery { apiService.getAllPedidos() } returns listOf(pedido)

        // When
        val result = repository.getAllOrdenes().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(1, result[0].size)
        assertEquals(2000, result[0][0].total)
    }

    @Test
    fun `getAllOrdenes failure returns empty list`() = runTest {
        // Given
        coEvery { apiService.getAllPedidos() } throws Exception("Network error")

        // When
        val result = repository.getAllOrdenes().toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0].isEmpty())
    }

    @Test
    fun `updateEstado success`() = runTest {
        // Given
        coEvery { apiService.updatePedidoEstado(1, "ENTREGADO") } returns Response.success(mockk())

        // When
        repository.updateEstado("1", "ENTREGADO")

        // Then
        coVerify { apiService.updatePedidoEstado(1, "ENTREGADO") }
    }

    @Test(expected = Exception::class)
    fun `updateEstado failure`() = runTest {
        // Given
        coEvery { apiService.updatePedidoEstado(1, "ENTREGADO") } returns Response.error(500, "".toResponseBody())

        // When
        repository.updateEstado("1", "ENTREGADO")
    }
}
