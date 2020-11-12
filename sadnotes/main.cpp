#include "dashboard.h"
#include "platform.h"
#include "dialog.h"
#include <filesystem>
#include <string>

namespace fs = std::filesystem;

#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    /* Set the directory to store note files.
     * For now, we are only supporting Linux.
     * TODO: Add Windows and Mac support.
     */
    fs::path notesPath;
    if(strcmp(PLATFORM_NAME, "linux") == 0) {
        notesPath = fs::path((std::string)getenv("HOME") + "/.local/share/sadnotes/notes");
    } else if (strcmp(PLATFORM_NAME, "windows") == 0) {
        // TODO
    } else if (strcmp(PLATFORM_NAME, "osx") == 0) {
        // TODO
    } else {
        Dialog dialog;
        dialog.setLabel("Operating system not supported.");
        dialog.show();
        a.quit();
        return a.exec();
    }
    /* Create the directory if it does not exist */
    if(!fs::exists(notesPath)) {
        fs::create_directories(notesPath);
    }
    Dashboard w = Dashboard(nullptr, notesPath);
    w.show();
    return a.exec();
}
