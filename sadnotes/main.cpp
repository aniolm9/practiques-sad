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
    /* Set the directory to store note files.
     * For now, we are only supporting Linux.
     * TODO: Add Windows and Mac support.
     */
    QString localPath;
    QString database = "sadnotes.db";
    if(strcmp(PLATFORM_NAME, "linux") == 0) {
        localPath = QString::fromStdString(getenv("HOME")) + "/.local/share/sadnotes/" ;
    } else if (strcmp(PLATFORM_NAME, "windows") == 0) {
        localPath = QString::fromStdString("C:/Users/") + qgetenv("USERNAME") + "/AppData/Local/";
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
    localPath = localPath + database;
    /* Connect to database */
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
    w.show();
    return a.exec();
}
