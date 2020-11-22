#include "include/platform.h"
#include "dashboard.h"
#include "database.h"
#include <QApplication>
#include <QTranslator>
#include <QObject>
#include <QMessageBox>

/* Application main method.
 * Checks the operating system.
 * Generates a database and a dashboard.
 */
int main(int argc, char *argv[]) {
    QApplication a(argc, argv);
    QTranslator qtTranslator;
    QTranslator sadNotesTranslator;
    QString localPath;
    QString database = "sadnotes.db";

    /* Load Qt translations */
    if (qtTranslator.load(QLocale::system(), "qtbase_")) {
        a.installTranslator(&qtTranslator);
    }
    /* Load sadnotes translations */
    if (sadNotesTranslator.load(QLocale::system(), ":/translations/sadnotes_")) {
        a.installTranslator(&sadNotesTranslator);
    }

    /* Set the directory to store note files.
     * For now, we are only supporting Linux and Windows.
     * TODO: Add Mac support.
     */
    if (strcmp(PLATFORM_NAME, "linux") == 0) {
        localPath = qgetenv("HOME") + "/.local/share/sadnotes/" ;
    } else if (strcmp(PLATFORM_NAME, "windows") == 0) {
        localPath = "C:/Users/" + qgetenv("USERNAME") + "/AppData/Local/";
    } else if (strcmp(PLATFORM_NAME, "osx") == 0) {
        // TODO
    } else {
        QMessageBox msgBox;
        msgBox.setWindowTitle(QObject::tr("Fatal error"));
        msgBox.setText(QObject::tr("Operating system not supported."));
        msgBox.setStandardButtons(QMessageBox::Close);
        msgBox.setDefaultButton(QMessageBox::Close);
        return msgBox.exec();
    }
    /* Create the parent directory if it does not exist */
    if(!QFile::exists(localPath)) {
        QDir().mkdir(localPath);
    }
    /* Connect to database */
    localPath = localPath + database;
    Database *db = new Database(localPath);
    if (!db->openConnection()) {
        QMessageBox msgBox;
        msgBox.setWindowTitle(QObject::tr("Fatal error"));
        msgBox.setText(QObject::tr("Couldn't connect to database."));
        msgBox.setStandardButtons(QMessageBox::Close);
        msgBox.setDefaultButton(QMessageBox::Close);
        return msgBox.exec();
    }
    /* Show the main window */
    Dashboard w = Dashboard(nullptr, db);
    QObject::connect(&a, &QApplication::focusChanged, &w, &Dashboard::setLastFocus);
    w.show();
    return a.exec();
}
