@rem
@echo off
@if "%DEBUG%"=="" @echo on
@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@rem Set local directory if required
@cd /d %~dp0

@rem Set JAVA_HOME if not already set
@if not "%JAVA_HOME%"=="" goto findJavaFromJavaHome

@rem Set JAVA_EXE if not already set
@if not "%JAVA_EXE%"=="" goto execute

@rem Try to find Java in PATH
@for /f "tokens=2 delims=:" %%j in ('where java 2^>nul') do (
    @set JAVA_EXE=%%j
    @goto execute
)

:findJavaFromJavaHome
@set JAVA_EXE=%JAVA_HOME%/bin/java.exe

:execute
@set DEFAULT_JVM_OPTS="-Xmx64m" "-Dfile.encoding=UTF-8"

@rem Setup the command line
@set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

@rem Execute Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
