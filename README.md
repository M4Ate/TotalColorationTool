# Total Coloration Tool

The Total Coloration Tool is a utility for viewing, computing, and comparing the total coloration of unweighted, non-directional graphs.

## Installation Guide

To run the provided JAR files, you need to have Java version 17 installed. Additionally, if you want to use the local ILP-Solver, you need to install either GLPK or HiGHS (or both).

### Installing HiGHS (recommended)

- Installation instructions for HiGHS can be found [here](https://ergo-code.github.io/HiGHS/dev/interfaces/cpp/).
- HiGHS can only be built from source, so you will need CMake as well as a C-Compiler like MSVC.
- After installation, the `highs` executable must be added to your PATH. See instructions [here](https://helpdeskgeek.com/windows-10/add-windows-path-environment-variable/).

### Installing GLPK

- Prebuilt binaries for Windows can be found [here](https://sourceforge.net/projects/winglpk/).
- On Linux, GLPK can be installed via the command:
  ```bash
  sudo apt-get install glpk-utils
  ``` 
- Alternatively GLPK can also be built from [source](http://ftp.gnu.org/gnu/glpk/).
- As with HiGHS the executable has to be added to PATH
  
