using DotnetTestServer.Models;
using Microsoft.EntityFrameworkCore;
namespace DotnetTestServer.Data;
public class ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : DbContext(options)
{
    public DbSet<User> Users { get; set; }
    public DbSet<Post> Posts { get; set; }
}
