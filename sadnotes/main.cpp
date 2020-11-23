/*
 * Copyright (c) 2020, Aniol Marti & Hugo Martínez
 *
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You should have received a copy of the License. If not,
 * see <https://opensource.org/licenses/BSD-3-Clause>.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
    QIcon icon(":/icons/dIcon.ico");
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
    a.setWindowIcon(icon);
    w.show();
    return a.exec();
}
