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
import { Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import axios from "axios";
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { LocalizationProvider, DatePicker, TimePicker } from '@mui/x-date-pickers';


const defaultTheme = createTheme();

export default function NovaObjava() {
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
  const [trainingTime, setTrainingTime] = useState(null);
    const [trainingDuration, setTrainingDuration] = useState(null);

  const [trainingType, setTrainingType] = useState('');
  const [bookingMessage, setBookingMessage] = useState('');
const validTimes = [
  '08:00', '08:30',
  '09:00', '09:30',
  '10:00', '10:30',
  '11:00', '11:30',
  '12:00', '12:30',
  '13:00', '13:30',
  '14:00', '14:30',
  '15:00', '15:30',
  '16:00', '16:30',
  '17:00', '17:30',
  '18:00', '18:30',
];

function parseTimeString(timeStr) {
  const [hours, minutes] = timeStr.split(':').map(Number);
  const date = new Date();
  date.setHours(hours, minutes, 0, 0);
  return date;
}

function isValidTime(date) {
  return validTimes.some(t => {
    const validDate = parseTimeString(t);
    return validDate.getHours() === date.getHours() &&
           validDate.getMinutes() === date.getMinutes();
  });
}


const handleBookingSubmit = async (event) => {
  event.preventDefault();
  
  if (!trainingDate || !trainingTime || !trainingType) {
    setBookingMessage("Please fill in all fields.");
    return;
  }

  const bookingData = {
    username: korisnicko_ime,
    date: trainingDate,
    time: trainingTime,
    type: trainingType,
  };

  try {
    const response = await fetch("http://localhost:8080/trainings/book", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
      },
      body: JSON.stringify(bookingData),
    });

    if (response.ok) {
      setBookingMessage("Training booked successfully!");
      setTrainingDate(null);
      setTrainingTime(null);
      setTrainingType('');
    } else {
      setBookingMessage("Failed to book training.");
    }
  } catch (error) {
    console.error("Booking error:", error);
    setBookingMessage("An error occurred during booking.");
  }
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
  <Box sx={{ marginTop: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
    <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
      <LockOutlinedIcon />
    </Avatar>
    <Typography component="h1" variant="h5">
      Book a Training
    </Typography>

    <Box component="form" onSubmit={handleBookingSubmit} noValidate sx={{ mt: 1 }}>
      <LocalizationProvider dateAdapter={AdapterDateFns}>
        <DatePicker
          label="Select a date"
          value={trainingDate}
          onChange={(newValue) => setTrainingDate(newValue)}
          renderInput={(params) => <TextField margin="normal" fullWidth {...params} />}
        />

        <TextField
  select
  fullWidth
  margin="normal"
  label="Select a time"
  value={trainingTime}
  onChange={(e) => setTrainingTime(e.target.value)}
>
  {validTimes.map((time) => (
    <MenuItem key={time} value={time}>
      {time}
    </MenuItem>
  ))}
</TextField>

      </LocalizationProvider>

      <TextField
        select
        fullWidth
        margin="normal"
        label="Duration"
        value={trainingDuration}
        onChange={(e) => setTrainingDuration(e.target.value)}
      >
        <MenuItem value={30}>30 minutes</MenuItem>
        <MenuItem value={60}>60 minutes</MenuItem>
      </TextField>

      <TextField
        margin="normal"
        fullWidth
        id="trainingType"
        label="Training Type"
        name="trainingType"
        value={trainingType}
        onChange={(e) => setTrainingType(e.target.value)}
      />

      <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }}>
        Book Training
      </Button>

      {bookingMessage && (
        <Typography color="success.main" sx={{ mt: 2 }}>
          {bookingMessage}
        </Typography>
      )}
    </Box>
  </Box>
</Container>



    </ThemeProvider>
    </div>
  );
}
