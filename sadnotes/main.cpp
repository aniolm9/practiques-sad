#include "platform.h"
#include "dashboard.h"
#include "dialog.h"
#include "database.h"
#include <QApplication>

/* Application main method.
 * Checks the operating system.
 * Generates a database and a dashboard.
 */
int main(int argc, char *argv[]) {
    QApplication a(argc, argv);
    QString localPath;
    QString database = "sadnotes.db";

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
        Dialog dialog;
        dialog.setLabel("Operating system not supported.");
        dialog.show();
        a.quit();
        return a.exec();
    }
    /* Create the parent directory if it does not exist */
    if(!QFile::exists(localPath)) {
        QDir().mkdir(localPath);
    }
    /* Connect to database */
    localPath = localPath + database;
    Database *db = new Database(localPath);
    if (!db->openConnection()) {
        Dialog dialog;
        dialog.setLabel("Couldn't connect to database.");
        dialog.show();
        a.quit();
        return a.exec();
    }
    /* Show the main window */
    Dashboard w = Dashboard(nullptr, db);
    QObject::connect(&a, &QApplication::focusChanged, &w, &Dashboard::setLastFocus);
    w.show();
    return a.exec();
}
