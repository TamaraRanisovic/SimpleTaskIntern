import * as React from 'react';
import { useEffect, useState, useRef } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import MenuItem from '@mui/material/MenuItem';
import { AppBar, Toolbar} from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import logo from './photos/posticon.png';
import { Dialog, DialogActions, DialogContent, DialogTitle,  List, ListItem, ListItemText, Divider } from '@mui/material';
import axios from "axios";
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { LocalizationProvider, DatePicker, TimePicker } from '@mui/x-date-pickers';


const defaultTheme = createTheme();

export default function ProfilKorisnika() {
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
  const [trainingTime, setTrainingTime] = useState(null);
  const [trainingDate, setTrainingDate] = useState(null);
  const [duration, setDuration] = useState(30);
  const [trainingType, setTrainingType] = useState('');
  const [trainer, setTrainer] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!trainingDate || !trainingTime || !duration || !trainingType || !korisnicko_ime) {
      setMessage('Please fill in all fields.');
      return;
    }

    const [hours, minutes] = trainingTime.split(':').map(Number);
    const dateTime = new Date(trainingDate);
    dateTime.setHours(hours, minutes, 0, 0);

    try {
      await axios.post('http://localhost:8080/trainings/save', {
        startTime: dateTime.toISOString(),
        duration,
        trainingType,
        korisnicko_ime,
      });
      setMessage('Training successfully created.');
    } catch (error) {
      setMessage('Error creating training: ' + (error.response?.data || error.message));
    }
  };

  const allowedTimes = Array.from({ length: 24 * 2 }, (_, i) => {
    const hour = Math.floor(i / 2);
    const minute = i % 2 === 0 ? '00' : '30';
    return `${hour.toString().padStart(2, '0')}:${minute}`;
  });


  const [trainings, setTrainings] = useState([]);
  const [selectedTrainingId, setSelectedTrainingId] = useState(null);
  const [bookingMessage, setBookingMessage] = useState('');

  useEffect(() => {
    if (trainingDate) {
      const formattedDate = trainingDate.toLocaleDateString('en-CA');
      axios.get(`http://localhost:8080/trainings/day?date=${formattedDate}`)
        .then((response) => setTrainings(response.data))
        .catch((error) => console.error('Error fetching trainings:', error));
    }
  }, [trainingDate]);

const handleBookingSubmit = (e) => {
  e.preventDefault();
  if (!selectedTrainingId || !korisnicko_ime) {
    setBookingMessage('Please select a training and make sure you are logged in.');
    return;
  }

  axios.post(`http://localhost:8080/trainings/book?trainingId=${selectedTrainingId}&username=${korisnicko_ime}`)
    .then(() => {
      setBookingMessage('Training booked successfully!');
    })
    .catch((error) => {
      const message = error.response?.data || 'Failed to book training.';
      setBookingMessage(`${message}`);
    });
};


function parseTimeString(timeStr) {
  const [hours, minutes] = timeStr.split(':').map(Number);
  const date = new Date();
  date.setHours(hours, minutes, 0, 0);
  return date;
}


const toDateObject = (dateArray) => {
  if (!Array.isArray(dateArray)) return null;
  const [year, month, day, hour, minute] = dateArray;
  return new Date(year, month - 1, day, hour, minute); // JS months are 0-based
};




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
              <Container component="main" maxWidth="sm">
      <CssBaseline />
      <Box component="form" onSubmit={handleSubmit} sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
          <FitnessCenterIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Create a New Training
        </Typography>

        <LocalizationProvider dateAdapter={AdapterDateFns}>
          <DatePicker
            label="Select a date"
            value={trainingDate}
            onChange={(newValue) => setTrainingDate(newValue)}
            renderInput={(params) => <TextField margin="normal" fullWidth {...params} required />}
          />
        </LocalizationProvider>

        <TextField
          select
          fullWidth
          margin="normal"
          label="Select Time"
          value={trainingTime}
          onChange={(e) => setTrainingTime(e.target.value)}
          required
        >
          {allowedTimes.map((time) => (
            <MenuItem key={time} value={time}>
              {time}
            </MenuItem>
          ))}
        </TextField>

        <TextField
          select
          fullWidth
          margin="normal"
          label="Duration"
          value={duration}
          onChange={(e) => setDuration(Number(e.target.value))}
          required
        >
          <MenuItem value={30}>30 minutes</MenuItem>
          <MenuItem value={60}>60 minutes</MenuItem>
        </TextField>

        <TextField
          fullWidth
          margin="normal"
          label="Training Type"
          value={trainingType}
          onChange={(e) => setTrainingType(e.target.value)}
          required
        />

        <TextField
          fullWidth
          margin="normal"
          label="Trainer"
          value={trainer}
          onChange={(e) => setTrainer(e.target.value)}
          required
        />

        <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }}>
          Create Training
        </Button>

        {message && (
          <Typography sx={{ mt: 2 }} color={message.includes('successfully') ? 'success.main' : 'error'}>
            {message}
          </Typography>
        )}
      </Box>
    </Container>



    </ThemeProvider>
    </div>
  );
}
