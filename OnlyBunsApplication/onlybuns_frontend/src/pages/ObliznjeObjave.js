import React, { useEffect, useState } from 'react';
import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material';
import { Link } from 'react-router-dom';
import logo from './photos/posticon.png';
import FavoriteIcon from '@mui/icons-material/Favorite';
import {Grid, Paper, IconButton } from '@mui/material';
import ChatBubbleOutlineIcon from '@mui/icons-material/ChatBubbleOutline';
import { useNavigate } from 'react-router-dom';
import { Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import axios from "axios";
import L from "leaflet";

const ObliznjeObjave = () => {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [role, setRole] = useState('');
  const token = localStorage.getItem('jwtToken'); // Get JWT token from localStorage
  const [openDialog, setOpenDialog] = useState(false);
  const [dialogMessage, setDialogMessage] = useState('');
  const navigate = useNavigate(); // React Router's navigate function
  const [openDialog2, setOpenDialog2] = useState(false);
  const [dialogMessage2, setDialogMessage2] = useState('');

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
    localStorage.removeItem('jwtToken');
    window.location.href = '/prijava'; // Redirect to login
  };

  const postIcon = L.icon({
    iconUrl:'http://localhost:8080/images/posticon.png',
    iconSize: [35, 35],
  });


  const locationIcon = L.divIcon({
    className: "",
    html: `
      <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#ff0000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M21 10c0 6-9 13-9 13S3 16 3 10a9 9 0 1 1 18 0z"/>
        <circle cx="12" cy="10" r="3" fill="#ff0000"/>
      </svg>
    `,
    iconSize: [40, 40],
    iconAnchor: [20, 40], // the tip of the pin touches the location
  });

  // Decode JWT token by calling the backend
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
        if (!data || data.Role !== "REGISTROVANI_KORISNIK") {
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
  

  const [userLocation, setUserLocation] = useState(null);
  const [nearbyPosts, setNearbyPosts] = useState([]);

  useEffect(() => {
    if (!username) return;

    console.log("Fetching user location for:", username);

    axios.get("http://localhost:8080/registrovaniKorisnik/lokacija", {
      headers: {
          Authorization: `Bearer ${token}`
      }
      })
          .then(response => {
            if (response.status === 200 && response.data.length === 2) {
                const [latitude, longitude] = response.data;
                console.log("User location received:", latitude, longitude);
                setUserLocation({ lat: latitude, lon: longitude });

                console.log("Fetching nearby posts...");
                return axios.get("http://localhost:8080/lokacija/nearby-posts", {
                  headers: {
                      Authorization: `Bearer ${token}`
                  }
              });
            } else {
                throw new Error("Invalid location response format");
            }
        })
        .then(response => {
            if (response.status === 204) {
                console.log("No nearby posts found.");
                setNearbyPosts([]);
            } else {
                console.log("Nearby posts received:", response.data);

                // Validate if response contains valid data
                if (Array.isArray(response.data) && response.data.length > 0) {
                    setNearbyPosts(response.data);
                } else {
                    console.warn("Invalid nearby posts data:", response.data);
                    setNearbyPosts([]);
                }
            }
        })
        .catch(error => {
            if (error.response) {
                if (error.response.status === 404) {
                    console.error("User not found.");
                } else if (error.response.status === 204) {
                    console.log("No nearby posts available.");
                } else {
                    console.error("Error fetching data:", error);
                }
            } else {
                console.error("Network error:", error);
            }
        });
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
              New post
            </Button>
            <Button onClick={handleOpenDialog2} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
              Trends
            </Button>
            <Button component={Link} to={`/obliznjeObjave/${username}`} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
              Nearby Posts
            </Button>
            <Button onClick={handleOpenDialog2} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
              Chat
            </Button>
            <Button onClick={logout} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
              Logout
            </Button>
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
      
      <div>
            <h2>Nearby posts on map</h2>
            {userLocation ? (
                <MapContainer center={[userLocation.lat, userLocation.lon]} zoom={13} style={{ height: "500px", width: "100%" }}>
                    <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

                    {/* Marker for user location */}
                    <Marker position={[userLocation.lat, userLocation.lon]} icon={locationIcon}>
                        <Popup>Your Location</Popup>
                    </Marker>

                    {/* Nearby posts markers */}
                    {nearbyPosts.length > 0 ? (
                        nearbyPosts.map((post, index) => {
                            const [lat, lon, postId, description, user, imageUrl] = post;
                            return lat && lon ? (
                                <Marker key={postId || index} position={[lat, lon]} icon={postIcon}>
                                    <Popup>
                                        <Link to={`/profilKorisnika/${user}`} style={{ color: 'black', textDecoration: 'none' }}>
                                          <b>{user}</b>
                                        </Link>
                                        {" "}
                                        <i>{description || "No description available"}</i> <br />
                                        
                                        {/* Make image clickable to navigate to the post view page */}
                                        {imageUrl && (
                                            <Link to={`/objavaPrikaz/${postId}`}>
                                                <img
                                                    src={`http://localhost:8080/images/${imageUrl}`}
                                                    alt={description}
                                                    style={{ width: "100px", marginTop: "5px", cursor: "pointer" }}
                                                />
                                            </Link>
                                        )}
                                    </Popup>
                                </Marker>
                            ) : null;
                        })
                    ) : (
                        <p>No nearby posts found.</p>
                    )}
                </MapContainer>
            ) : (
                <p>Loading map...</p>
            )}
        </div>
        

    </div>
  );
};

export default ObliznjeObjave;
