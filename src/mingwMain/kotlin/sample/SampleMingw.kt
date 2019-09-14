package sample

import cnames.structs.GLFWwindow
import glfw.*
import kotlinx.cinterop.*
import platform.opengl32.GL_COLOR_BUFFER_BIT
import platform.opengl32.glClear
import platform.opengl32.glViewport

fun main() {
    // From https://www.glfw.org/docs/latest/quick_guide.html
    if (glfwInit() == GLFW_FALSE) {
        println("Error initializing glfw")
        return
    }

    glfwSetErrorCallback(staticCFunction(::error))

    val window = glfwCreateWindow(640, 480, "NESEmu", null, null)
    if (window == null) {
        println("Can't create window")
        glfwTerminate()
        return
    }
    glfwMakeContextCurrent(window)
    // Initialize extensions here

    glfwSetKeyCallback(window, staticCFunction(::keyCallback))
    glfwSwapInterval(1)

    while (glfwWindowShouldClose(window) == GLFW_FALSE) {
        memScoped {
            val height = alloc<IntVar>()
            val width = alloc<IntVar>()
            glfwGetFramebufferSize(window, width.ptr, height.ptr)
            glViewport(0, 0, width.value, height.value)
        }
        glClear(GL_COLOR_BUFFER_BIT)
        glfwSwapBuffers(window)
        glfwPollEvents()
    }

    glfwDestroyWindow(window)
    glfwTerminate()
}

fun error(@Suppress("UNUSED_PARAMETER") error: Int, description: CPointer<ByteVar>?) {
    println("*ERROR RECEIVED*: ${description?.toKString()}")
}

@Suppress("UNUSED_PARAMETER")
fun keyCallback(window: CPointer<GLFWwindow>?, key: Int, scanCode: Int, action: Int, mods: Int) {
    if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
        glfwSetWindowShouldClose(window, GLFW_TRUE)
}