package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.local.dao.CarritoDao
import com.pasteleria_app.pasteleria_app.data.local.entities.ProductoEntity
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CarritoRepositoryImplTest {

    private lateinit var repository: CarritoRepositoryImpl
    private val dao: CarritoDao = mockk(relaxed = true)

    @Before
    fun setup() {
        repository = CarritoRepositoryImpl(dao)
    }

    @Test
    fun `obtenerProductos success`() = runTest {
        val entity = ProductoEntity(
            id = 1, nombre = "Pastel", precio = 1000, imagen = 0, cantidad = 1, mensaje = "msg"
        )
        coEvery { dao.obtenerProductos() } returns flowOf(listOf(entity))

        val result = repository.obtenerProductos().toList()

        assertEquals(1, result.size)
        assertEquals(1, result[0].size)
        assertEquals("Pastel", result[0][0].nombre)
    }

    @Test
    fun `agregarProducto nuevo`() = runTest {
        val producto = Producto(
            id = 1, productoId = 1L, nombre = "Pastel", precio = 1000, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO"
        )
        coEvery { dao.obtenerProductoPorNombre("Pastel") } returns null

        repository.agregarProducto(producto)

        coVerify { dao.agregarProducto(any()) }
    }

    @Test
    fun `agregarProducto existente`() = runTest {
        val producto = Producto(
            id = 1, productoId = 1L, nombre = "Pastel", precio = 1000, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO"
        )
        val entity = ProductoEntity(
            id = 1, nombre = "Pastel", precio = 1000, imagen = 0, cantidad = 1, mensaje = "msg"
        )
        coEvery { dao.obtenerProductoPorNombre("Pastel") } returns entity

        repository.agregarProducto(producto)

        coVerify { dao.actualizarProducto(any()) }
    }

    @Test
    fun `actualizarProducto calls dao`() = runTest {
        val producto = Producto(
            id = 1, productoId = 1L, nombre = "Pastel", precio = 1000, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO"
        )
        repository.actualizarProducto(producto)
        coVerify { dao.actualizarProducto(any()) }
    }

    @Test
    fun `eliminarProducto calls dao`() = runTest {
        val producto = Producto(
            id = 1, productoId = 1L, nombre = "Pastel", precio = 1000, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO"
        )
        repository.eliminarProducto(producto)
        coVerify { dao.eliminarProducto(any()) }
    }

    @Test
    fun `vaciarCarrito calls dao`() = runTest {
        repository.vaciarCarrito()
        coVerify { dao.vaciarCarrito() }
    }

    @Test
    fun `obtenerProductoPorNombre found`() = runTest {
        val entity = ProductoEntity(
            id = 1, nombre = "Pastel", precio = 1000, imagen = 0, cantidad = 1, mensaje = "msg"
        )
        coEvery { dao.obtenerProductoPorNombre("Pastel") } returns entity

        val result = repository.obtenerProductoPorNombre("Pastel")

        assertNotNull(result)
        assertEquals("Pastel", result?.nombre)
    }

    @Test
    fun `obtenerProductoPorNombre not found`() = runTest {
        coEvery { dao.obtenerProductoPorNombre("Pastel") } returns null

        val result = repository.obtenerProductoPorNombre("Pastel")

        assertNull(result)
    }
    
    @Test
    fun `obtenerProductos empty`() = runTest {
        coEvery { dao.obtenerProductos() } returns flowOf(emptyList())
        val result = repository.obtenerProductos().toList()
        assertEquals(1, result.size)
        assertEquals(0, result[0].size)
    }
    
    @Test
    fun `agregarProducto existente actualiza cantidad`() = runTest {
        val producto = Producto(
            id = 1, productoId = 1L, nombre = "Pastel", precio = 1000, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO", cantidad = 2
        )
        val entity = ProductoEntity(
            id = 1, nombre = "Pastel", precio = 1000, imagen = 0, cantidad = 1, mensaje = "msg"
        )
        coEvery { dao.obtenerProductoPorNombre("Pastel") } returns entity

        repository.agregarProducto(producto)

        coVerify { 
            dao.actualizarProducto(match { 
                it.cantidad == 3 // 1 existing + 2 new
            }) 
        }
    }
}
