namespace DotnetTestServer.Models
{
    public class Post
    {
        public int Id { get; set; }
        public string ComposerName { get; set; }
        public string Title { get; set; }
        public string PostMessage { get; set; }
        public DateTime DateTime { get; set; }
    }
}
