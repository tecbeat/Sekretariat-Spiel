@echo off
 :Start2 
    cls
    goto Start
    :Start
    echo --------------------------------------
    echo          Conventional Commits
    echo --------------------------------------  
    echo.          
    echo Choose from the list given below:
    echo.    
    echo [1] FIX: (Bugfix)
    echo [2] STYLE: (Design staff)
    echo [3] REFRACTOR: (Name change)
    echo [4] FEAT: (Added a feature)
    echo [5] TEST: (Just for tests)
    echo [6] DOCS: (Edit Docu)
    echo [7] ADD: (New staff)
    echo.    

    set /a one=1
    set /a two=2
    set /a three=3
    set /a four=4
    set /a five=5
    set /a six=6
    set /a seven=7

    set input=
    set /p input= Enter your choice: 
    echo.    
    if %input% equ %one% goto fix if NOT goto def
    if %input% equ %two% goto sty if NOT goto def
    if %input% equ %three% goto ref if NOT goto def
    if %input% equ %four% goto fea if NOT goto def
    if %input% equ %five% goto tes if NOT goto def
    if %input% equ %six% goto doc if NOT goto def
    if %input% equ %seven% goto add if NOT goto def
    
    goto def
    exit

:fix
    cls
    echo Type what you have done!
    set msg=FIX:
    set /p ART=FIX: 
    goto git
    exit

:sty
    cls
    echo Type what you have done!
    set msg=STYLE:
    set /p ART=STYLE: 
    goto git
    exit

:ref 
    cls
    echo Type what you have done!
    set msg=REFRACTOR:
    set /p ART=REFRACTOR: 
    goto git
    exit

:fea
    cls
    echo Type what you have done!
    set msg=FEAT:
    set /p ART=FEAT: 
    goto git
    exit

:tes
    cls
    echo Type what you have done!
    set msg=TEST:
    set /p ART=TEST: 
    goto git
    exit

:doc
    cls
    echo Type what you have done!
    set msg=DOCS:
    set /p ART=DOCS: 
    goto git
    exit

:add
    cls
    echo Type what you have done!
    set msg=ADD:
    set /p ART=ADD: 
    goto git
    exit

:def
    cls
    echo Type what you have done!
    set msg=DEFAULT:
    set /p ART=DEFAULT: 
    goto git
    exit

:git
    cls
    set gitmsg=%msg% (%ART%)
    echo Your commit message is: %gitmsg%
    cd ..
    git add .
    git commit -m "%gitmsg%"
    git push origin master
    exit









