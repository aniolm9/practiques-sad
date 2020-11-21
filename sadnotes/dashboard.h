#ifndef DASHBOARD_H
#define DASHBOARD_H

#include <QMainWindow>
#include <QTextBrowser>
#include "database.h"
#include "dialog.h"
#include "note.h"
#include "constants.h"
#include "smallnote.h"
#include <string>

QT_BEGIN_NAMESPACE
namespace Ui {
    class Dashboard;
}
QT_END_NAMESPACE

class Dashboard : public QMainWindow {
    Q_OBJECT

    public:
        Dashboard(QWidget *parent = nullptr, Database *db = nullptr);
        ~Dashboard();

    private:
        QObject *lastFocused;
        Ui::Dashboard *ui;
        Database *database;
        QVector<SmallNote*> sns;
        void updateView();

    public slots:
        int saveNote(int id, QString name, QString data);
        void setLastFocus(QWidget *old, QWidget *now);

    private slots:
        void on_newNote_clicked();
        void on_saveAll_clicked();
        void on_remove_clicked();
};
#endif // DASHBOARD_H
