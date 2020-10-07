namespace CocoxToMakeFile
{
    using System.IO;
    public class FileUtils
    {

        public string[] getFiles(string sourceDir, string mask)
        {
            return Directory.GetFiles(sourceDir, mask);
        }

        public void copyFiles(string src, string dst)
        {
            File.Copy(src, dst);


        }
        public bool exists(string fileName)
        {
            return File.Exists(fileName);
        }

        public DirectoryInfo createFolders(string baseDir)
        {
            return Directory.CreateDirectory(baseDir);
        }




    }
}