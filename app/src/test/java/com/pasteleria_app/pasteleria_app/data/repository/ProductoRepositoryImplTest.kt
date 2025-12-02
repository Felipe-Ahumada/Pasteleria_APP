package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.model.Producto
import com.pasteleria_app.pasteleria_app.data.network.ApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ProductoRepositoryImplTest {

    private lateinit var repository: ProductoRepositoryImpl
    private val apiService: ApiService = mockk()

    @Before
    fun setup() {
        repository = ProductoRepositoryImpl(apiService)
    }

    @Test
    fun `getProductos success`() = runTest {
        val p = Producto(
            id = 1L, codigoProducto = "code", nombre = "Pastel", precio = 1000.0, descripcion = "desc",
            imagenPrincipal = "url", imagenesDetalle = null, stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO"
        )
        coEvery { apiService.getProductos() } returns listOf(p)

        val result = repository.getProductos()

        assertEquals(1, result.size)
        assertEquals("Pastel", result[0].nombre)
    }

    @Test
    fun `getProductos error returns empty`() = runTest {
        coEvery { apiService.getProductos() } throws Exception("Error")

        val result = repository.getProductos()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getProducto success`() = runTest {
        val p = Producto(
            id = 1L, codigoProducto = "code", nombre = "Pastel", precio = 1000.0, descripcion = "desc",
            imagenPrincipal = "url", imagenesDetalle = null, stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO"
        )
        coEvery { apiService.getProducto(1) } returns Response.success(p)

        val result = repository.getProducto(1)

        assertNotNull(result)
        assertEquals("Pastel", result?.nombre)
    }

    @Test
    fun `getProducto not found`() = runTest {
        coEvery { apiService.getProducto(1) } returns Response.error(404, "".toResponseBody())

        val result = repository.getProducto(1)

        assertNull(result)
    }

    @Test
    fun `getProducto exception`() = runTest {
        coEvery { apiService.getProducto(1) } throws Exception("Error")

        val result = repository.getProducto(1)

        assertNull(result)
    }

    @Test
    fun `getProductosPorCategoria success`() = runTest {
        val p = Producto(
            id = 1L, codigoProducto = "code", nombre = "Pastel", precio = 1000.0, descripcion = "desc",
            imagenPrincipal = "url", imagenesDetalle = null, stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO"
        )
        coEvery { apiService.getProductosPorCategoria(1) } returns listOf(p)

        val result = repository.getProductosPorCategoria(1)

        assertEquals(1, result.size)
    }

    @Test
    fun `getProductosPorCategoria error returns empty`() = runTest {
        coEvery { apiService.getProductosPorCategoria(1) } throws Exception("Error")

        val result = repository.getProductosPorCategoria(1)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `createProducto success`() = runTest {
        val p = Producto(
            id = 1L, codigoProducto = "code", nombre = "Pastel", precio = 1000.0, descripcion = "desc",
            imagenPrincipal = "url", imagenesDetalle = null, stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO"
        )
        coEvery { apiService.createProducto(any()) } returns Response.success(p)

        val result = repository.createProducto(p)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `createProducto failure`() = runTest {
        coEvery { apiService.createProducto(any()) } returns Response.error(500, "".toResponseBody())

        val result = repository.createProducto(mockk(relaxed = true))

        assertTrue(result.isFailure)
    }

    @Test
    fun `deleteProducto success`() = runTest {
        coEvery { apiService.deleteProducto(1) } returns Response.success(Unit)

        val result = repository.deleteProducto(1)

        assertTrue(result.isSuccess)
    }
}
