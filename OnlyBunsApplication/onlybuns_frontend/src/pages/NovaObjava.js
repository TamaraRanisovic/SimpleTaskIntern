import * as React from 'react';
import { useEffect, useState, useRef } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import MenuItem from '@mui/material/MenuItem';
import { AppBar, Toolbar} from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import logo from './photos/posticon.png';
import { MapContainer, TileLayer, Marker, useMapEvents, useMap } from 'react-leaflet';
import { Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import axios from "axios";


const defaultTheme = createTheme();

export default function NovaObjava() {
  const [opis, setOpis] = useState('');
  const [g_sirina, setG_sirina] = useState('');
  const [g_duzina, setG_duzina] = useState('');
  const [slika, setSlika] = useState('dasdas');
  const [datum_objave, setDatumObjave] = useState(new Date().toISOString());

  const [korisnicko_ime, setKorisnickoIme] = useState('');

  const [errorMessage, setErrorMessage] = useState('');
  const token = localStorage.getItem('jwtToken'); // Get JWT token from localStorage
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [role, setRole] = useState('');
  const formData = new FormData();
  const [message, setMessage] = useState('');
  const [file, setFile] = useState(null);
  const [selectedPosition, setSelectedPosition] = useState(null);
  const navigate = useNavigate();
  const [successMessage, setSuccessMessage] = useState('');
  const isMounted = useRef(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [dialogMessage, setDialogMessage] = useState('');
  const navigate2 = useNavigate(); // React Router's navigate function to redirect
  const [street, setStreet] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
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
    navigate2('/prijava');
  };

  const logout = () => {
    localStorage.removeItem("jwtToken"); // Remove token

    // Redirect to login page
    window.location.href = "/prijava";  // or use `useNavigate` from React Router v6
  };

  const handleGeocode = async (street, city, state) => {
    if (!street || !city || !state) return; // Ensure all fields are filled before making the request
  
    try {
      const response = await axios.post("http://localhost:8080/geocode/get-coordinates", {
        ulica: street,
        grad: city,
        drzava: state
      });
  
      if (response.data.latitude && response.data.longitude) {
        setG_sirina(response.data.latitude);
        setG_duzina(response.data.longitude);
      } else {
        setErrorMessage("Could not fetch coordinates for this address.");
      }
    } catch (error) {
      setErrorMessage("Error occurred while fetching coordinates.");
    }
  };

  const UpdateMapCenter = ({ latitude, longitude }) => {
    const map = useMapEvents({
      click(e) {
        handleReverseGeocode(e.latlng.lat, e.latlng.lng);
      },
    });
  
    useEffect(() => {
      map.setView([latitude, longitude], map.getZoom());
    }, [latitude, longitude, map]);
  
    return <Marker position={[latitude, longitude]} />;
  };
  
  // Automatically call `handleGeocode` when user types in address fields
  useEffect(() => {
    handleGeocode(street, city, state);
  }, [street, city, state]);


  // 📌 Function to Reverse Geocode (Coordinates → Address)
  const handleReverseGeocode = async (lat, lon) => {
    try {
      // Ensure lat/lon values are valid before making a request
      if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
        setErrorMessage("Invalid latitude or longitude.");
        return;
      }
  
      const response = await axios.get(`http://localhost:8080/geocode/get-address`, {
        params: { latitude: lat, longitude: lon }
      });
  
      if (response.data.ulica && response.data.grad && response.data.drzava) {
        setStreet(response.data.ulica);
        setCity(response.data.grad);
        setState(response.data.drzava);
      } else {
        setErrorMessage("Address not found for the selected location.");
      }
    } catch (error) {
      setErrorMessage("Error occurred while fetching address.");
    }
  };
  

    
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
        setKorisnickoIme(data.Username);
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
  
  const handlePhotoChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      setFile(selectedFile);
      setSlika(selectedFile.name); // Set `slika` to the file name for reference
    }
  };

  const handlePhotoUpload = async () => {
    const formData = new FormData();
    formData.append('file', file);
    
    try {
      const response = await fetch("http://localhost:8080/images/upload", {
        method: "POST",
        body: formData
      });
      
      if (response.ok) {
        console.log("Photo uploaded successfully");
      } else {
        console.error("Photo upload failed");
      }
    } catch (error) {
      console.error("Error uploading photo:", error);
    }
  };

  function LocationMarker() {
    const map = useMapEvents({
      click(e) {
        const { lat, lng } = e.latlng;
        setG_sirina(lat);
        setG_duzina(lng);
        handleReverseGeocode(lat, lng);
      },
    });
  
    return g_sirina && g_duzina ? <Marker position={[g_sirina, g_duzina]} /> : null;
  }

  useEffect(() => {
    isMounted.current = true;
    return () => {
      isMounted.current = false;
    };
  }, []);


  const handleSubmit = async (event) => {
    event.preventDefault();
    
    if (!korisnicko_ime || !opis || !g_sirina || !g_duzina || !slika) {
      setErrorMessage('Enter valid data.');
      return;
    }
    setErrorMessage('');

    setDatumObjave(new Date().toISOString());
    const objava = { korisnicko_ime, opis, slika, datum_objave, 
      lokacijaDTO: {
        ulica: street,
        grad: city,
        drzava: state
    } };
    const token = localStorage.getItem("jwtToken");

    try {
      const response = await fetch("http://localhost:8080/objava/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
         },
        body: JSON.stringify(objava)
      });

      if (response.ok) {
        console.log("New post created successfully");
        
        // Now, upload the photo after the objava is created
        await handlePhotoUpload();

        setSuccessMessage("New post created successfully. Redirecting to feed...");
  
        setTimeout(() => {
          if (isMounted.current) {
            setSuccessMessage("");
            navigate("/prijavljeniKorisnikPregled");
          }
        }, 15000);
      } else {
        console.error("Failed to create post");
      }
    } catch (error) {
      console.error("Error creating post:", error);
    }
  };



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
    <ThemeProvider theme={defaultTheme}>
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
              <Button component={Link} to={`/obliznjeObjave/${korisnicko_ime}`}  color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                Nearby Posts
              </Button>
              <Button onClick={handleOpenDialog2} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                Chat
              </Button>
              {token && korisnicko_ime ? ( 
                <Button onClick={logout} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold'}}>
                  Logout
                </Button>
              ) : (<></>)}
            </Box>
          <Box sx={{ display: 'flex', gap: 2, mr: 2, alignItems: 'center' }}>
            {token && korisnicko_ime ? ( 
              <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                Welcome, {korisnicko_ime}
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
      <Container component="main" maxWidth="xs">
      <CssBaseline />
      <Box sx={{ marginTop: 4, display: "flex", flexDirection: "column", alignItems: "center" }}>
        <Typography component="h1" variant="h5" sx={{ marginBottom: 2 }}>
          Create New Post
        </Typography>
        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>          
          <TextField fullWidth required label="Description" value={opis} onChange={(e) => setOpis(e.target.value)} sx={{ mb: 1.5 }} />
          <Typography variant="body2" sx={{ mb: 1 }}>
            Select location on the map or enter an address manually.
          </Typography>

          {/* Address Fields */}
          <TextField fullWidth label="Street Name" value={street} onChange={(e) => setStreet(e.target.value)} sx={{ mb: 1.5 }} />
          <TextField fullWidth label="City" value={city} onChange={(e) => setCity(e.target.value)} sx={{ mb: 1.5 }} />
          <TextField fullWidth label="State" value={state} onChange={(e) => setState(e.target.value)} sx={{ mb: 1.5 }} />

          {/* Map */}
          <MapContainer center={[g_sirina, g_duzina]} zoom={13} style={{ height: "200px", width: "100%", marginBottom: "15px" }}>
            <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
            <UpdateMapCenter latitude={g_sirina} longitude={g_duzina} />
          </MapContainer>

          {/* Coordinates */}
          <TextField fullWidth InputProps={{ readOnly: true }} label="Latitude" value={g_sirina} onChange={(e) => setG_sirina(parseFloat(e.target.value))} sx={{ mb: 1.5 }} />
          <TextField fullWidth InputProps={{ readOnly: true }} label="Longitude" value={g_duzina} onChange={(e) => setG_duzina(parseFloat(e.target.value))} sx={{ mb: 1.5 }} />

          {/* File Upload */}
          <div style={{ marginBottom: '15px' }}>
            <label>Upload Photo:</label>
            <input type="file" accept="image/*" onChange={handlePhotoChange} />
          </div>
          <Button
            type="submit"
            sx={{ padding: '5px 10px', borderRadius: '15px', fontSize: '1rem', fontWeight: 'bold', mt: 2, mb: 3 }}
            fullWidth
            variant="contained"
            color="secondary"
          >
            Publish
          </Button>
          {errorMessage && (
            <Typography color="error" sx={{ mb: 4 }} variant="body2" gutterBottom>
              {errorMessage}
            </Typography>
          )}
          {successMessage && (
        <Typography color="green" sx={{ mb: 4 }} variant="body2" gutterBottom>
        {successMessage}
      </Typography>
        )}
        </Box>
      </Box>
    </Container>


    </ThemeProvider>
    </div>
  );
}
