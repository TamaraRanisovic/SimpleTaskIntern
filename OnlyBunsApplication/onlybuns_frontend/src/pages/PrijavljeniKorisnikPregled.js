import React, { useEffect, useState } from 'react';
import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material';
import { Link } from 'react-router-dom';
import logo from './photos/posticon.png';
import FavoriteIcon from '@mui/icons-material/Favorite';
import {Grid, Paper, IconButton } from '@mui/material';
import ChatBubbleOutlineIcon from '@mui/icons-material/ChatBubbleOutline';
import { useNavigate } from 'react-router-dom';
import { Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';

const PrijavljeniKorisnikPregled = () => {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [role, setRole] = useState('');
  const token = localStorage.getItem('jwtToken'); // Get JWT token from localStorage
  const [rabbitPosts, setRabbitPosts] = useState([]); // State to store posts from the database
  const [openDialog, setOpenDialog] = useState(false);
  const [dialogMessage, setDialogMessage] = useState('');
  const [openDialog2, setOpenDialog2] = useState(false);
  const [dialogMessage2, setDialogMessage2] = useState('');
  const navigate = useNavigate(); // React Router's navigate function to redirect

  const handleOpenDialog2 = () => {
    setDialogMessage2("Feature Coming Soon...");
    setOpenDialog2(true);
  };

  const handleCloseDialog2 = () => {
    setOpenDialog2(false);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    navigate('/prijava');
  };

    const logout = () => {
      localStorage.removeItem("jwtToken"); // Remove token
  
      // Redirect to login page
      window.location.href = "/prijava";  // or use `useNavigate` from React Router v6
    };

  // Function to decode the JWT token by calling the backend endpoint
  useEffect(() => {
    if (!token) {
      setDialogMessage('No user found. Please log in.');
      setOpenDialog(true);
      setTimeout(() => {
        navigate('/prijava'); // Redirect to login after 15 seconds
      }, 15000); // Delay redirection to allow user to read the message
      return;
    }

    fetch('http://localhost:8080/auth/decodeJwt', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: token,
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Failed to decode token');
      }
      return response.json();
    })
    .then(data => {
      if (!data || data.Role !== "REGISTERED_USER") {
        setDialogMessage('Unauthorized access. Redirecting to login...');
        setOpenDialog(true);
        setTimeout(() => {
          navigate('/prijava');
        }, 15000);
        return;
      }

      // Set user data only if role is valid
      setEmail(data.Email);
      setUsername(data.Username);
      setRole(data.Role);
    })
    .catch(error => {
      console.error('Error decoding JWT token:', error);
      setDialogMessage('Session expired or invalid token. Please log in again.');
      setOpenDialog(true);
      setTimeout(() => {
        navigate('/prijava');
      }, 15000);
    });
}, [token, navigate]);



    useEffect(() => {
      if (username) { // Only fetch if username is available
        fetch("http://localhost:8080/objava/feed", {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`, // Uncomment if you have a token
          }
        })
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.json();
          })
          .then(data => {
            console.log('Fetched data:', data); // Log data to check if it's correct
            setRabbitPosts(Array.isArray(data) ? data : []); // Ensure data is an array
          })
          .catch(error => {
            console.error('Error fetching posts:', error);
          });
      }
    }, [username]);

    

  return (
    <div>
      {/* Dialog box for showing the message */}
      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>Notification</DialogTitle>
        <DialogContent>{dialogMessage}</DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="primary">
            OK
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={openDialog2} onClose={handleCloseDialog2}>
        <DialogTitle>Notification</DialogTitle>
        <DialogContent>{dialogMessage2}</DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog2} color="primary">
            OK
          </Button>
        </DialogActions>
      </Dialog>
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
          <Box sx={{ display: 'flex', gap: 2 }}>
            <Button component={Link} to="/prijavljeniKorisnikPregled" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
              Feed
            </Button>
            <Button component={Link} to="/novaObjava" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
              Book a training
            </Button>
            <Button onClick={handleOpenDialog2} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
              Trends
            </Button>
            <Button component={Link} to={`/obliznjeObjave/${username}`}  color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
              Nearby Posts
            </Button>
            <Button onClick={handleOpenDialog2} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
              Chat
            </Button>
            {token && username ? ( 
              <Button onClick={logout} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold'}}>
                Logout
              </Button>
            ) : (<></>)}
          </Box>
          <Box sx={{ display: 'flex', gap: 2, mr: 2, alignItems: 'center' }}>
            {token && username ? ( 
              <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                Welcome, {username}
              </Typography>
            ) : (
              <>
                <Button component={Link} to="/registracija" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  Sign In
                </Button>
                <Button component={Link} to="/prijava" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  Log In
                </Button>
              </>
            )}
          </Box>
        </Toolbar>
      </AppBar>
      {/* Rabbit Post Cards */}
      <Grid container spacing={3} sx={{ mt: 4 }}>
      {rabbitPosts.map((post, index) => (
        <Grid item xs={12} sm={6} md={3} key={index}>
          {/* Link to Detailed View */}
          <Link to={`/objavaPrikaz/${post.id}`} style={{ textDecoration: 'none' }}>
            <Paper elevation={6} sx={{ padding: 3, borderRadius: '15px', textAlign: 'center', boxShadow: '0 12px 24px rgba(0, 0, 0, 0.1)' }}>
              <img
                src={`http://localhost:8080/images/${post.slika}`}
                alt={post.opis}
                style={{ height: '240px', width: '100%', borderRadius: '10px', marginBottom: '15px' }}
              />

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
                 <Link to={`/profilKorisnika/${post.korisnicko_ime}`} style={{ textDecoration: "none" }}>
                            <Typography sx={{ fontWeight: "bold" }}>{post.korisnicko_ime}</Typography>
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
        

    </div>
  );
};

export default PrijavljeniKorisnikPregled;
