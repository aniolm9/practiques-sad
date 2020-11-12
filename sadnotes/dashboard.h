#ifndef DASHBOARD_H
#define DASHBOARD_H

#include <QMainWindow>
#include <string>

QT_BEGIN_NAMESPACE
namespace Ui { class Dashboard; }
QT_END_NAMESPACE

class Dashboard : public QMainWindow
{
    Q_OBJECT

public:
    Dashboard(QWidget *parent = nullptr, std::string notesPath = "");
    ~Dashboard();

private slots:
    void on_newNote_clicked();

private:
    Ui::Dashboard *ui;
    std::string notesPath;
};
#endif // DASHBOARD_H
