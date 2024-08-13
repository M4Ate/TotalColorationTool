Total Coloration Tool
Total Coloration Tool is a tool that helps with viewing, computing and comparing the total coloration of unweighted non-directional graphs.

Installation Guide
To run the provided JAR files you need to have Java version ?? installed.
In addition, if you want to use the local ILP-Solver, you need to install either GLPK or HiGHS (or both).

Installing HiGHS:
  Installation instructions for HiGHS can be found here: https://ergo-code.github.io/HiGHS/dev/interfaces/cpp/
  HiGHS can only be built from source, so you will need CMAKE as well as a C-Compiler like MSVC
  After installation the highs executable also has to be added to PATH (https://helpdeskgeek.com/windows-10/add-windows-path-environment-variable/)

Installing GLPK:
  Prebuilt binaries for windows can be found here:     https://sourceforge.net/projects/winglpk/
  On Linux GLPK can be installed via     sudo apt-get install glpk-utils
  Alternatively GLPK can also be built from source:     http://ftp.gnu.org/gnu/glpk/
  As with HiGHS the executable has to be added to PATH
  
