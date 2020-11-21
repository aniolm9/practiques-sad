#ifndef DASHBOARD_H
#define DASHBOARD_H

#include <QMainWindow>
#include "database.h"
#include "dialog.h"
#include "note.h"
#include "constants.h"
#include "smallnote.h"

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
        void createNote(int id = constants::NEW_ID, QString name = "", QString data = "");

    public slots:
        int saveNote(int id, QString name, QString data);
        void setLastFocus(QWidget *old, QWidget *now);
        void recoverFocus();

    private slots:
        void on_newNote_clicked();
        void on_saveAll_clicked();
        void on_remove_clicked();
        void on_open_clicked();
};
#endif // DASHBOARD_H
