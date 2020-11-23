QT       += core gui sql

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

CONFIG += c++17

# You can make your code fail to compile if it uses deprecated APIs.
# In order to do so, uncomment the following line.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

SOURCES += \
    database.cpp \
    main.cpp \
    dashboard.cpp \
    note.cpp \
    smallnote.cpp

HEADERS += \
    include/constants.h \
    dashboard.h \
    database.h \
    note.h \
    include/platform.h \
    smallnote.h

FORMS += \
    ui/dashboard.ui \
    ui/note.ui

TRANSLATIONS += \
    resources/l10n/sadnotes_ca.ts

RESOURCES += \
    resources/resources.qrc

# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target
