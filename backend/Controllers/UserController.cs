namespace DotnetTestServer.Data;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using DotnetTestServer.Models;
using System.Collections.Generic;
using System.Threading.Tasks;
using BCrypt.Net;
[ApiController]
[Route("api/[controller]")]
public class UserController(ApplicationDbContext context) : ControllerBase
{
    private readonly ApplicationDbContext _context = context;

    [HttpGet]
    public async Task<ActionResult<IEnumerable<User>>> GetUsers()
    {
        return await _context.Users.ToListAsync();
    }




    [HttpPost("signup")]
    public async Task<ActionResult<User>> AddUser([FromBody] SignUpRequest signUpRequest)
    {
        // Validate the sign-up request
        if (signUpRequest == null || string.IsNullOrEmpty(signUpRequest.Name) || string.IsNullOrEmpty(signUpRequest.Password))
        {
            return BadRequest("Invalid sign-up request");
        }

        // Check if the username is already taken
        if (await _context.Users.AnyAsync(u => u.Name == signUpRequest.Name))
        {
            return BadRequest("Username is already taken");
        }

        // Hash and salt the password
        string hashedPassword = BCrypt.HashPassword(signUpRequest.Password);

        // Create a new user based on the sign-up request
        var newUser = new User
        {
            Name = signUpRequest.Name,
            Password = hashedPassword, // Store the hashed password
                                       // set other properties as needed
        };

        // Add the new user to the context
        _context.Users.Add(newUser);

        // Save changes to the database
        await _context.SaveChangesAsync();

        // For security reasons, you may want to avoid returning the password in the response.
        // Create a DTO (Data Transfer Object) for the response if needed.

        return CreatedAtAction(nameof(GetUsers), new { id = newUser.Id }, newUser);
    }


[HttpPost("login")]
public async Task<ActionResult<User>> Login([FromBody] LoginRequest loginRequest)
{
      if (!ModelState.IsValid)
    {
        return BadRequest(ModelState);
    }
    // Validate the login request
    if (loginRequest == null || string.IsNullOrEmpty(loginRequest.Name) || string.IsNullOrEmpty(loginRequest.Password))
    {
        return BadRequest("Invalid login request");
    }

    // Find the user by name
    var user = await _context.Users.FirstOrDefaultAsync(u => u.Name == loginRequest.Name);

    if (user == null)
    {
        // User not found
        return NotFound(new { message = "User not found" });
    }

    if (!BCrypt.Verify(loginRequest.Password, user.Password))
    {
        // Incorrect password
        return Unauthorized(new { message = "Wrong password" });
    }

    // At this point, the username and password are correct
    // You might generate and return a token or session information if needed

    // Return the user as JSON
    return Ok(user);
}



}
