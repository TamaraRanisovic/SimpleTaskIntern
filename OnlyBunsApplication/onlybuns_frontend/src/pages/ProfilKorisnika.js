import React from 'react';
import { AppBar, Toolbar, Avatar, Typography, Button, Box, Grid, Paper, IconButton, Snackbar } from '@mui/material';
import { Link } from 'react-router-dom';
import logo from './photos/posticon.png';
import FavoriteIcon from '@mui/icons-material/Favorite';
import { useEffect, useState, useRef } from 'react';
import ChatBubbleOutlineIcon from '@mui/icons-material/ChatBubbleOutline';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';

export default function ProfilKorisnika() {
  const { username } = useParams();
  const [rabbitPosts, setRabbitPosts] = useState([]); // State to store posts from the database
  const [user, setUser] = useState(null);
  const token = localStorage.getItem('jwtToken'); // Get JWT token from localStorage
  const navigate = useNavigate();  // For redirection if the user is not logged in
  const [openDialog, setOpenDialog] = useState(false);  // To control Dialog visibility
  const [dialogMessage, setDialogMessage] = useState('');  // To store dialog message
  const [redirectToLogin, setRedirectToLogin] = useState(false);  // Flag to trigger redirection
  const isMounted = useRef(true);
  const timeoutIdRef = useRef(null); // To store the timeout ID
  const [email, setEmail] = useState('');
  const [korisnickoIme, setKorisnickoIme] = useState('');
  const [role, setRole] = useState('');
  const [openDialog2, setOpenDialog2] = useState(false);
  const [dialogMessage2, setDialogMessage2] = useState('');

  const handleOpenDialog2 = () => {
    setDialogMessage2("Feature Coming Soon...");
    setOpenDialog2(true);
  };

  const handleCloseDialog2 = () => {
    setOpenDialog2(false);
  };

  const logout = () => {
    localStorage.removeItem("jwtToken"); // Remove token

    // Redirect to login page
    window.location.href = "/prijava";  // or use `useNavigate` from React Router v6
  };

    useEffect(() => {

      fetch('http://localhost:8080/auth/decodeJwt', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: token,
      })
        .then(response => response.json())
        .then(data => {
          if (data) {
            setEmail(data.Email);
            setKorisnickoIme(data.Username);
            setRole(data.Role);
          }
        })
        .catch(error => {
          console.error('Error decoding JWT token:', error);
        });
    }, [token]);

   useEffect(() => {
        isMounted.current = true;
        return () => {
          isMounted.current = false;
        };
      }, []);
 

  useEffect(() => {
    // Fetch user details
    fetch(`http://localhost:8080/registrovaniKorisnik/username/${username}`) 
      .then(response => response.json())
      .then(data => setUser(data))
      .catch(error => console.error('Error fetching user:', error));

    // Fetch user posts
    fetch(`http://localhost:8080/objava/user/${username}`, {
      headers: {
        //'Authorization': `Bearer ${token}`, // Include JWT token if needed for authentication
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
  }, []);

  const handleDialogClose = () => {
    setOpenDialog(false);  // Close the dialog
    setRedirectToLogin(false);
    isMounted.current = false;
  };

const handleButtonClick = async () => {
    const token = localStorage.getItem('jwtToken');  // Check for JWT token in localStorage

    if (token) {
      try {
        // Send the token to the backend for decoding
        const response = await fetch('http://localhost:8080/auth/decodeJwt', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: token,
        });

        if (!response.ok) {
          setDialogMessage('Session expired. Please log in again.');
          setOpenDialog(true);  // Show the Dialog
        }

        // If token is decoded successfully, show the dialog box for the upcoming feature
        setDialogMessage('Feature Coming Soon: Follow functionality');
        setOpenDialog(true);  // Show the Dialog

      } catch (error) {
        console.error('Error decoding JWT:', error);
        setDialogMessage('Failed to decode the token. Please log in again.');
        setOpenDialog(true);  // Show the Dialog
      }
    } else {
      // If no token, show dialog box and set flag to redirect
      setDialogMessage('No user found. Please log in.');
      setOpenDialog(true);  // Show the Dialog
      setRedirectToLogin(true);  // Set the flag to trigger redirection
      setTimeout(() => {
        if (isMounted.current) {
          navigate("/prijava");
        }
      }, 15000);
      return;
    }
  };




  if (!user) return <Typography>Loading profile...</Typography>;


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
          <Box sx={{ display: 'flex', gap: 2 }}>
          {role === "REGISTROVANI_KORISNIK" ? ( 
              <Box sx={{ display: 'flex', gap: 2 }}>
                <Button component={Link} to="/prijavljeniKorisnikPregled" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  Feed
                </Button>
                <Button component={Link} to="/novaObjava" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  New post
                </Button>
                <Button onClick={handleOpenDialog2} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  Trends
                </Button>
                <Button component={Link} to={`/obliznjeObjave/${korisnickoIme}`}  color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  Nearby Posts
                </Button>
                <Button onClick={handleOpenDialog2} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  Chat
                </Button>
              </Box>
            ) : role === "ADMIN_SISTEMA" ? (
              <>
                <Button component={Link} to="/prijavljeniKorisnikPregled" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  ADVERTISING
                </Button>
                <Button onClick={handleOpenDialog2} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  Trends
                </Button>
              </>
            ) : (
              <>
              </>
            )}
            {token && korisnickoIme ? ( 
              <Button onClick={logout} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold'}}>
                Logout
              </Button>
            ) : (<></>)}
          </Box>
          <Box sx={{ display: 'flex', gap: 2, mr: 2, alignItems: 'center' }}>
            {token && korisnickoIme ? ( 
              <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                Welcome, {korisnickoIme}
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

      {/* Page Content */}
      <Box sx={{ padding: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        
      <Paper
  elevation={3}
  sx={{
    textAlign: 'center',
    padding: 3, // Reduced padding
    bgcolor: '#fff',
    borderRadius: '16px',
    boxShadow: '0px 4px 12px rgba(0,0,0,0.1)',
    maxWidth: 320, // Reduced maxWidth
    width: '100%',
  }}
>
  <Avatar
  src={logo}
  alt={user.korisnickoIme || 'User'}
    sx={{ width: 80, height: 80, margin: 'auto', border: '3px solid #b4a7d6' }} // Reduced avatar size
  />

  <Typography variant="h6" sx={{ fontWeight: 'bold', mt: 1.5 }}>{user.korisnickoIme || 'Unknown User'}</Typography>



  {/* Follow Stats */}
  <Box sx={{ display: 'flex', justifyContent: 'center', gap: 3, mt: 2 }}>
    <Box sx={{ textAlign: 'center' }}>
      <Typography variant="body1" sx={{ fontWeight: 'bold', color: '#b4a7d6' }}>
        {user.num_followers ?? 0}
      </Typography>
      <Typography variant="body2" sx={{ color: '#555' }}>Followers</Typography>
    </Box>

    <Box sx={{ textAlign: 'center' }}>
      <Typography variant="body1" sx={{ fontWeight: 'bold', color: '#b4a7d6' }}>
        {user.num_following ?? 0}
      </Typography>
      <Typography variant="body2" sx={{ color: '#555' }}>Following</Typography>
    </Box>
  </Box>

  {/* Follow Button */}
   <>
      <Button
        variant="contained"
        color="secondary"
        onClick={handleButtonClick}  // Trigger the handleButtonClick function on button click
        sx={{
          mt: 2,
          padding: '6px 14px',
          borderRadius: '16px',
          fontSize: '0.8rem',
          fontWeight: 'bold',
        }}
      >
        Follow
      </Button>

      {/* Dialog for showing the message */}
      <Dialog open={openDialog} onClose={handleDialogClose}>
        <DialogTitle>Notification</DialogTitle>
        <DialogContent>
          {dialogMessage}  {/* Show the appropriate message based on the user's state */}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDialogClose} color="primary">
            Close
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

    </>
</Paper>



          {/* Rabbit Post Cards */}
        <Grid container spacing={3} sx={{ mt: 0 }}>
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
