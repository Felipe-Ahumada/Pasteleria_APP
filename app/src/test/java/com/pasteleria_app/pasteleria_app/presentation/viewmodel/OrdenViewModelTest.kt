package com.pasteleria_app.pasteleria_app.presentation.viewmodel

import com.pasteleria_app.pasteleria_app.data.preferences.UserPreferences
import com.pasteleria_app.pasteleria_app.domain.model.Orden
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.domain.repository.OrdenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class OrdenViewModelTest {

    private lateinit var viewModel: OrdenViewModel
    private val repository: OrdenRepository = mockk(relaxed = true)
    private val prefs: UserPreferences = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = OrdenViewModel(repository, prefs)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cargarOrdenes success`() = runTest {
        coEvery { prefs.userEmailFlow } returns flowOf("email")
        coEvery { repository.getOrdenes("email") } returns flowOf(emptyList())

        viewModel.cargarOrdenes()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.getOrdenes("email") }
    }

    @Test
    fun `cargarOrdenes no email`() = runTest {
        coEvery { prefs.userEmailFlow } returns flowOf(null)

        viewModel.cargarOrdenes()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { repository.getOrdenes(any()) }
    }

    @Test
    fun `cargarOrdenPorId success`() = runTest {
        val orden = mockk<Orden>(relaxed = true)
        coEvery { repository.getOrden("1") } returns flowOf(orden)

        viewModel.cargarOrdenPorId("1")
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.ordenSeleccionada.value)
    }

    @Test
    fun `crearOrden success`() = runTest {
        coEvery { prefs.userEmailFlow } returns flowOf("email")
        val p = Producto(
            id = 1, productoId = 1L, nombre = "P", precio = 100, imagenUrl = "url",
            codigoProducto = "c", descripcion = "d", stock = 10, stockCritico = 5, categoria = null, estado = "A"
        )
        
        val id = viewModel.crearOrden(
            listOf(p), 100, "Dir", "Com", "Fecha", "Retiro"
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(id)
        coVerify { repository.crearOrden(any(), "email") }
    }

    @Test
    fun `crearOrden no email returns empty`() = runTest {
        coEvery { prefs.userEmailFlow } returns flowOf(null)
        
        val id = viewModel.crearOrden(
            emptyList(), 0, "", "", "", ""
        )
        
        assertEquals("", id)
    }
    
    @Test
    fun `crearOrden despacho`() = runTest {
        coEvery { prefs.userEmailFlow } returns flowOf("email")
        val p = Producto(
            id = 1, productoId = 1L, nombre = "P", precio = 100, imagenUrl = "url",
            codigoProducto = "c", descripcion = "d", stock = 10, stockCritico = 5, categoria = null, estado = "A"
        )
        
        viewModel.crearOrden(
            listOf(p), 100, "Dir", "Com", "Fecha", "Despacho"
        )
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { 
            repository.crearOrden(match { 
                it.tipoEntrega == "Despacho" && it.direccionEntrega == "Dir"
            }, "email") 
        }
    }
    
    @Test
    fun `crearOrden retiro`() = runTest {
        coEvery { prefs.userEmailFlow } returns flowOf("email")
        val p = Producto(
            id = 1, productoId = 1L, nombre = "P", precio = 100, imagenUrl = "url",
            codigoProducto = "c", descripcion = "d", stock = 10, stockCritico = 5, categoria = null, estado = "A"
        )
        
        viewModel.crearOrden(
            listOf(p), 100, "Dir", "Com", "Fecha", "Retiro en tienda"
        )
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { 
            repository.crearOrden(match { 
                it.tipoEntrega == "Retiro en tienda" && it.direccionEntrega == "Retiro en tienda"
            }, "email") 
        }
    }
    
    @Test
    fun `cargarOrdenPorId updates state`() = runTest {
        val orden = mockk<Orden>(relaxed = true)
        coEvery { repository.getOrden("1") } returns flowOf(orden)
        
        viewModel.cargarOrdenPorId("1")
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(orden, viewModel.ordenSeleccionada.value)
    }
    
    @Test
    fun `cargarOrdenes updates state`() = runTest {
        coEvery { prefs.userEmailFlow } returns flowOf("email")
        val list = listOf(mockk<Orden>())
        coEvery { repository.getOrdenes("email") } returns flowOf(list)
        
        viewModel.cargarOrdenes()
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(list, viewModel.ordenes.value)
    }
    
    @Test
    fun `init state is empty`() = runTest {
        assertEquals(emptyList<Orden>(), viewModel.ordenes.value)
        assertEquals(null, viewModel.ordenSeleccionada.value)
    }
}
