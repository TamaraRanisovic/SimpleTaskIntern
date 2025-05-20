import * as React from 'react';
import { useEffect, useState, useRef } from 'react';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import MenuItem from '@mui/material/MenuItem';
import { AppBar, Toolbar} from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import logo from './photos/posticon.png';
import { Dialog, DialogActions, DialogContent, DialogTitle,  List, ListItem, ListItemText, Divider } from '@mui/material';
import axios from "axios";
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { LocalizationProvider, DatePicker, TimePicker } from '@mui/x-date-pickers';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import { useParams } from 'react-router-dom';
import {
  Container,
  Typography,
  Box,
  CircularProgress,
  Card,
  CardContent,
  Grid,
  Avatar,
} from '@mui/material';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import PersonIcon from '@mui/icons-material/Person';
import EventIcon from '@mui/icons-material/Event';
import CancelScheduleSendIcon from '@mui/icons-material/CancelScheduleSend';

const defaultTheme = createTheme();

export default function TrainingView() {
  const [korisnicko_ime, setKorisnickoIme] = useState('');

  const token = localStorage.getItem('jwtToken'); // Get JWT token from localStorage
  const [email, setEmail] = useState('');
  const [role, setRole] = useState('');

  const navigate = useNavigate();
  const isMounted = useRef(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [dialogMessage, setDialogMessage] = useState('');
  const navigate2 = useNavigate(); // React Router's navigate function to redirect
  const [openDialog2, setOpenDialog2] = useState(false);
  const [dialogMessage2, setDialogMessage2] = useState('');

  const [trainingDate, setTrainingDate] = useState(null);
  const [trainings, setTrainings] = useState([]);
  const [selectedTrainingId, setSelectedTrainingId] = useState(null);
  const [bookingMessage, setBookingMessage] = useState('');
  const [viewType, setViewType] = useState('daily'); // 'daily' or 'weekly'


const toDateObject = (dateArray) => {
  if (!Array.isArray(dateArray)) return null;
  const [year, month, day, hour, minute] = dateArray;
  return new Date(year, month - 1, day, hour, minute); // JS months are 0-based
};

  const { id } = useParams();
  const [training, setTraining] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get(`http://localhost:8080/trainings/${id}`)
      .then(response => {
        setTraining(response.data);
        setLoading(false);
      })
      .catch(() => {
        setLoading(false);
      });
  }, [id]);



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
        if (!data || data.Role !== "TRAINER") {
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



  useEffect(() => {
    isMounted.current = true;
    return () => {
      isMounted.current = false;
    };
  }, []);


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
                Book a training
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
              {training ? (
  <Container maxWidth="sm">
    <Box sx={{ mt: 6 }}>
      <Card sx={{ p: 3, borderRadius: 3, boxShadow: 4 }}>
        <CardContent>
          <Grid container spacing={2} alignItems="center" justifyContent="center">
            <Grid item>
              <Avatar sx={{ bgcolor: 'primary.main', width: 56, height: 56 }}>
                <FitnessCenterIcon />
              </Avatar>
            </Grid>
            <Grid item>
              <Typography variant="h4" gutterBottom>
                {training.trainingType}
              </Typography>
            </Grid>
          </Grid>

          <Box sx={{ mt: 3 }}>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  <EventIcon sx={{ mr: 1 }} />
                  <Typography variant="subtitle1">
                    {toDateObject(training.startTime).toLocaleString('en-US', {
                    year: 'numeric',     // e.g., 2025
                    month: 'long',       // e.g., May
                    day: 'numeric',      // e.g., 19
                    hour: '2-digit',     // e.g., 02 PM
                    minute: '2-digit'    // e.g., :30
                    })}
                  </Typography>
                </Box>
              </Grid>

              <Grid item xs={12} sm={6}>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  <AccessTimeIcon sx={{ mr: 1 }} />
                  <Typography variant="subtitle1">
                    Duration: {training.duration} min
                  </Typography>
                </Box>
              </Grid>

              <Grid item xs={12} sm={6}>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  <PersonIcon sx={{ mr: 1 }} />
                  <Typography variant="subtitle1">
                    Trainer: {training.trainer}
                  </Typography>
                </Box>
              </Grid>

              <Grid item xs={12} sm={6}>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  <CancelScheduleSendIcon sx={{ mr: 1 }} />
                  <Typography variant="subtitle1">
                    Cancel up to {training.cancelDeadline} hrs before
                  </Typography>
                </Box>
              </Grid>
            </Grid>
          </Box>
        </CardContent>
      </Card>
    </Box>
  </Container>
) : (
  <Typography variant="h6" align="center" sx={{ mt: 6 }}>
    Loading training data...
  </Typography>
)}


    </ThemeProvider>
    </div>
  );
}
