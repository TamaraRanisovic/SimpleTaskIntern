import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Typography, Paper, Box } from '@mui/material';
import { AppBar, Toolbar, Button, IconButton, List, ListItem, ListItemText  } from '@mui/material';
import { Link } from 'react-router-dom';
import logo from './photos/posticon.png';
import ChatBubbleOutlineIcon from "@mui/icons-material/ChatBubbleOutline";
import FavoriteIcon from "@mui/icons-material/Favorite";
import { Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';

const ObjavaPrikaz = () => {
  const { postId } = useParams();
  const [post, setPost] = useState(null);
  const [email, setEmail] = useState('');
  const [korisnickoIme, setKorisnickoIme] = useState('');
  const [role, setRole] = useState('');
  const token = localStorage.getItem('jwtToken'); // Get JWT token from localStorage
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
    // Fetch post details using postId
    fetch(`http://localhost:8080/objava/${postId}`)
      .then(response => response.json())
      .then(data => setPost(data))
      .catch(error => console.error('Error fetching post:', error));
  }, [postId]);

  if (!post) {
    return <Typography>Loading...</Typography>;
  }

  return (
    <div>
      <Dialog open={openDialog2} onClose={handleCloseDialog2}>
              <DialogTitle>Notification</DialogTitle>
              <DialogContent>{dialogMessage2}</DialogContent>
              <DialogActions>
                <Button onClick={handleCloseDialog2} color="primary">
                  OK
                </Button>
              </DialogActions>
        </Dialog>
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
  <Paper
      elevation={6}
      sx={{
        padding: 3,
        borderRadius: "15px",
        boxShadow: "0 12px 24px rgba(0, 0, 0, 0.1)",
        display: "flex",
        alignItems: "flex-start",
        width: "700px",
        margin: "auto",
        mt: 2,
      }}
    >
      {/* Post Section */}
      <Box sx={{ width: "60%", textAlign: "center" }}>
        <img
          src={`http://localhost:8080/images/${post.slika}`}
          alt={post.opis}
          style={{ height: "310px", width: "100%", borderRadius: "10px", marginBottom: "15px" }}
        />
        
        {/* Likes and Comments Row */}
        <Box sx={{ display: "flex", justifyContent: "center", gap: 1, alignItems: "center", mb: 1 }}>
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <IconButton sx={{ color: "#e91e63" }}>
              <FavoriteIcon />
            </IconButton>
            <Typography variant="body2" sx={{ fontWeight: "bold" }}>
              {post.broj_lajkova}
            </Typography>
          </Box>
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <IconButton color="secondary">
              <ChatBubbleOutlineIcon />
            </IconButton>
            <Typography variant="body2" sx={{ fontWeight: "bold" }}>
              {post.broj_komentara}
            </Typography>
          </Box>
        </Box>

        {/* Description Row */}
        <Box sx={{ display: "flex", flexDirection: "column", alignItems: "flex-start" }}>
          <Link to={`/profilKorisnika/${post.korisnicko_ime}`} style={{ textDecoration: "none" }}>
            <Typography sx={{ fontWeight: "bold" }}>{post.korisnicko_ime}</Typography>
          </Link>
          <Typography
            variant="body2"
            color="textSecondary"
            sx={{ fontStyle: "italic", whiteSpace: "normal", wordWrap: "break-word" }}
          >
            {post.opis}
          </Typography>
        </Box>
      </Box>

      <Box sx={{ 
  width: "45%", 
  paddingLeft: 2, 
  borderLeft: "1px solid #ddd", 
  maxHeight: "400px",  // Set a fixed height for the scrollable area
  overflowY: "auto",   // Enable vertical scrolling
}}>
  <Typography variant="h6" sx={{ fontWeight: "bold", mb: 1.5 }}>
    Comments
  </Typography>
  <List>
    {post.komentari.map((komentar, index) => (
      <ListItem
        key={index}
        alignItems="flex-start"
        sx={{
          transition: "background-color 0.3s ease",
          "&:hover": { backgroundColor: "#f5f5f5" }, // Light grey hover effect
          borderRadius: "10px",
          padding: "8px 12px",
        }}
      >
        <ListItemText
          primary={
            <Link
              to={`/profilKorisnika/${komentar.korisnicko_ime}`}
              style={{ textDecoration: "none", color: "inherit" }}
            >
              <Typography sx={{ fontWeight: "bold", "&:hover": { color: "#1976d2" } }}>
                {komentar.korisnicko_ime}
              </Typography>
            </Link>
          }
          secondary={
            <>
              <Typography variant="body2" color="textPrimary">
                {komentar.opis}
              </Typography>
            </>
          }
        />
      </ListItem>
    ))}
  </List>
</Box>

    </Paper>
    </div>
  );
};

export default ObjavaPrikaz;
