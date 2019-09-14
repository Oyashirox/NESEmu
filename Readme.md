# NESEmu

Side-project to create a NES Emulator using Kotlin/Native, and OpenGL.

This file is kind of a memo of the steps I took to get where it is now.

## Chapter 0: Using Kotlin/Native

Nothing specific to do, except launching IntelliJ and creating a default Native project.

Everything happens in the `build.gradle` file.

## Chapter 1: Importing GLFW as a cinterop library

- Download GLFW from the [Official website](https://www.glfw.org/) and put it in the `CLib` folder. (I removed unused
folders in this repo)
- Write the `.def` file in `src/nativeInterop/cinterop` as explain [here](https://kotlinlang.org/docs/reference/native/c_interop.html).
  We're going for a static link for now.
     ```
        headers = glfw3.h glfw3native.h
        staticLibraries = libglfw3.a
        package = glfw
        libraryPaths = CLib/glfw-3.3.bin.WIN64/lib-mingw-w64
        linkerOpts.mingw = -lgdi32
    ```
  It's pretty self explanatory: We're defining the header files (filenames only, include directory is defined by gradle),
  the lib, which package the kotlin wrapper will be in, and the path to the lib (We can't add this in the gradle yet.
  I saw some solutions but this is simpler for now). Last but not least, we define the linker options: As stated in the 
  warning section, on Windows we need to link against gdi32.
    
- Configure Gradle to generate the .klib as explain [here](https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#cinterop-support)
    ```groovy
    compilations.main.cinterops {
        glfw {
            includeDirs.allHeaders("CLib/glfw-3.3.bin.WIN64/include/GLFW")
        }
    }
    ```
  This allows `cinterop` command to know where to find the headers declared in the .def file. The name
  of the interop (here `glfw`) means that it refers to the `glfw.def` file in `src/nativeInterop/cinterop`.
- If everything went fine, you should be able to `import glfw` in your kotlin sources.
  You can check the generated wrapper in `build/classes/kotlin/mingw/main/NESEmu-cinterop-glfw.klib-build/kotlin/glfw/glfw.kt`

**Warnings**
- Be careful, GLFW needs gdi32 to be linked on Windows ([doc](https://www.glfw.org/docs/latest/build_guide.html))
- IntelliJ may let all references to the lib in red. Invalidating cache & restart worked.

## Chapter 2: Default GLFW code to display a window

Just follow the official [quickstart](https://www.glfw.org/docs/latest/quick_guide.html) and use the official kotlin doc
to map types between C and Kotlin.
- [Mapping struct and union](https://kotlinlang.org/docs/tutorials/native/mapping-struct-union-types-from-c.html)
- [Mapping function pointers](https://kotlinlang.org/docs/tutorials/native/mapping-function-pointers-from-c.html) (for callbacks)
- [Mapping Strings](https://kotlinlang.org/docs/tutorials/native/mapping-strings-from-c.html)
- [Int pointer](https://jonnyzzz.com/blog/2019/01/14/kn-intptr/)