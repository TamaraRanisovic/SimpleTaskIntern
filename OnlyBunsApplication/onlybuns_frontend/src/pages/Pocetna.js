import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box, Grid, Paper, IconButton } from '@mui/material';
import { Link } from 'react-router-dom';
import logo from './photos/posticon.png';
import FavoriteIcon from '@mui/icons-material/Favorite';
import { useEffect, useState } from 'react';
import ChatBubbleOutlineIcon from '@mui/icons-material/ChatBubbleOutline';

export default function HomePage() {
  const [rabbitPosts, setRabbitPosts] = useState([]); // State to store posts from the database

  useEffect(() => {
    fetch('http://localhost:8080/objava', {
      headers: {
        'Content-Type': 'application/json',
      }
    })
      .then(response => response.json())
      .then(data => {
        setRabbitPosts(data); // Store the fetched posts in state
      })
      .catch(error => {
        console.error('Error fetching posts:', error);
      });
  });

  return (
    <div>
      {/* Navigation Bar */}
      <AppBar position="static" sx={{ bgcolor: '#b4a7d6' }}>
        <Toolbar sx={{ justifyContent: 'space-between' }}>
          <Link to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', color: 'inherit' }}>
          <Box sx={{ display: 'flex', alignItems: 'center', ml: 2 }}>
            <img src={logo} alt="OnlyBuns Logo" style={{ height: '40px', marginRight: '10px' }} />
            <Typography variant="h5" component="div" sx={{ fontWeight: 'bold' }}>
              OnlyBuns
            </Typography>
          </Box>
          </Link>
          <Box sx={{ display: 'flex', gap: 2, mr: 2 }}>
            <Button component={Link} to="/registracija" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
              Sign In
            </Button>
            <Button component={Link} to="/prijava" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
              Log In
            </Button>
          </Box>
        </Toolbar>
      </AppBar>

      {/* Main Content */}
      <Box sx={{ padding: 3, textAlign: 'center' }}>
        <Typography variant="h4" sx={{ mt: 5, mb: 2 }}>
          Welcome to OnlyBuns!
        </Typography>
        <Typography variant="body1" sx={{ mb: 4 }}>
          Share and enjoy the cutest photos of rabbits with the OnlyBuns community.
        </Typography>
        <Button variant="contained" component={Link} to="/prijava" color="secondary" sx={{ padding: '10px 20px', borderRadius: '20px', fontSize: '1.2rem', fontWeight: 'bold' }}>
          Start Sharing
        </Button>

        {/* Rabbit Post Cards */}
        <Grid container spacing={3} sx={{ mt: 4 }}>
        {rabbitPosts.map((post, index) => (
          <Grid item xs={12} sm={6} md={3} key={index}>
            <Link to={`/objavaPrikaz/${post.id}`} style={{ textDecoration: 'none' }}>
            <Paper elevation={6} sx={{ padding: 3, borderRadius: '15px', textAlign: 'center', boxShadow: '0 12px 24px rgba(0, 0, 0, 0.1)' }}>
            <img src={`http://localhost:8080/images/${post.slika}`} alt={post.opis} style={{height: '240px', width: '100%', borderRadius: '10px', marginBottom: '15px' }} />
              
              {/* Likes and Comments Row */}
              <Box sx={{ display: 'flex', justifyContent: 'center', gap: 1, alignItems: 'center', mb: 1 }}>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  
                  <IconButton sx={{ color: '#e91e63' }}>
                    <FavoriteIcon />
                  </IconButton>
                  <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                    {post.broj_lajkova}
                  </Typography>
                </Box>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  <IconButton color="secondary">
                    <ChatBubbleOutlineIcon />
                  </IconButton>
                  <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                    {post.broj_komentara}
                  </Typography>
                </Box>
              </Box>

              {/* Description Row */}
              <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
                <Link to={`/profilKorisnika/${post.korisnicko_ime}`} style={{ textDecoration: 'none' }}>
                  <Typography sx={{ fontWeight: 'bold' }}>
                    {post.korisnicko_ime}
                  </Typography>
                </Link>
                <Typography variant="body2" color="textSecondary" sx={{ fontStyle: 'italic', whiteSpace: 'normal', wordWrap: 'break-word' }}>
                  {post.opis}
                </Typography>
              </Box>

            </Paper>
            </Link>
          </Grid>
        ))}
    </Grid>
      </Box>
    </div>
  );
}
