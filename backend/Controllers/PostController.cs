using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using DotnetTestServer.Models;
using System.Collections.Generic;
using System.Threading.Tasks;
using DotnetTestServer.Data;

[ApiController]
[Route("api/[controller]")]
public class PostController : ControllerBase
{
    private readonly ApplicationDbContext _context;

    public PostController(ApplicationDbContext context)
    {
        _context = context;
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<Post>>> GetPosts()
    {
        return await _context.Posts.ToListAsync();
    }

[HttpPost("add")]
public async Task<ActionResult<Post>> AddPost(Post postRequest)
{
    if (postRequest == null)
    {
        return BadRequest("Invalid post data");
    }

    // Convert DateTime to UTC before saving to the database
    var newPost = new Post
    {
        ComposerName = postRequest.ComposerName,
        Title = postRequest.Title,
        PostMessage = postRequest.PostMessage,
        DateTime = postRequest.DateTime.ToUniversalTime()  // Ensure DateTime is in UTC
    };

    _context.Posts.Add(newPost);
    await _context.SaveChangesAsync();

    return CreatedAtAction(nameof(GetPost), new { id = newPost.Id }, newPost);
}


    [HttpGet("{id}")]
    public async Task<ActionResult<Post>> GetPost(int id)
    {
        var post = await _context.Posts.FindAsync(id);

        if (post == null)
        {
            return NotFound();
        }

        return Ok(post);
    }
    [HttpDelete("{id}")]
public async Task<ActionResult<Post>> DeletePost(int id)
{
    var post = await _context.Posts.FindAsync(id);

    if (post == null)
    {
        return NotFound();
    }

    _context.Posts.Remove(post);
    await _context.SaveChangesAsync();

    return Ok(post);
}

}
