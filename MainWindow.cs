using System;
using Gtk;
using UI = Gtk.Builder.ObjectAttribute;

namespace CocoxToMakeFile
{
    class MainWindow : Window
    {
      

        public MainWindow() : this(new Builder("MainWindow.glade")) { }

        private MainWindow(Builder builder) : base(builder.GetObject("MainWindow").Handle)
        {
            builder.Autoconnect(this);

        }

        private void Window_DeleteEvent(object sender, DeleteEventArgs a)
        {
            Application.Quit();
        }

        private void Button1_Clicked(object sender, EventArgs a)
        {
           
        }
    }
}
