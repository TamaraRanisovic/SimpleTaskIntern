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
import { AppBar, Toolbar, FormControl, InputLabel, Select} from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';

import { Dialog, DialogActions, DialogContent, DialogTitle} from '@mui/material';
import axios from "axios";
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { LocalizationProvider, DatePicker } from '@mui/x-date-pickers';


const defaultTheme = createTheme();

export default function BookTrainingTrainer() {
  const [korisnicko_ime, setKorisnickoIme] = useState('');
  const token = localStorage.getItem('jwtToken'); // Get JWT token from localStorage
  const [email, setEmail] = useState('');
  const [role, setRole] = useState('');
  const navigate = useNavigate();
  const isMounted = useRef(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [dialogMessage, setDialogMessage] = useState('');
  const navigate2 = useNavigate();
  const [trainingTime, setTrainingTime] = useState(null);
  const [trainingDate, setTrainingDate] = useState(null);
  const [duration, setDuration] = useState(30);
  const [trainingType, setTrainingType] = useState('');
  const [trainer, setTrainer] = useState('');
  const [message, setMessage] = useState('');
  const [cancelDeadline, setCancelDeadline] = useState('');

const handleSubmit = async (e) => {
  e.preventDefault();

  if (!trainingDate || !trainingTime || !duration || !trainingType || !trainer || !cancelDeadline) {
    setMessage('Please fill in all fields.');
    return;
  }

  const [hours, minutes] = trainingTime.split(':').map(Number);
  const dateTime = new Date(trainingDate);
  dateTime.setHours(hours, minutes, 0, 0);

  const now = new Date();
  if (dateTime <= now) {
    setMessage('Selected time must be in the future.');
    return;
  }

  try {
    await axios.post('http://localhost:8080/trainings/create', {
      startTime: dateTime.toISOString(),
      duration: parseInt(duration),
      trainingType,
      trainer,
      cancelDeadline: parseInt(cancelDeadline),
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

  
  const handleCloseDialog = () => {
    setOpenDialog(false);
    navigate2('/login');
  };

  const logout = () => {
    localStorage.removeItem("jwtToken"); 

    window.location.href = "/login"; 
  };

    
  useEffect(() => {
      if (!token) {
        setDialogMessage('No user found. Please log in.');
        setOpenDialog(true);
        setTimeout(() => {
          navigate('/login'); // Redirect to login after 15 seconds
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
            navigate('/login');
          }, 15000);
          return;
        }
  
        // Set user data only if role is valid
        setEmail(data.Email);
        setKorisnickoIme(data.Username);
        setRole(data.Role);
        setTrainer(data.Username);
      })
      .catch(error => {
        console.error('Error decoding JWT token:', error);
        setDialogMessage('Session expired or invalid token. Please log in again.');
        setOpenDialog(true);
        setTimeout(() => {
          navigate('/login');
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
    <ThemeProvider theme={defaultTheme}>
      <AppBar position="static" sx={{ bgcolor: '#4FC3F7' }}>
        <Toolbar sx={{ justifyContent: 'space-between' }}>
          <Link to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', color: 'inherit' }}>
            <Box sx={{ display: 'flex', alignItems: 'center', ml: 2 }}>
        <Avatar sx={{ m: 1, bgcolor: '#283593' }}>
          <FitnessCenterIcon />
        </Avatar>              
        <Typography variant="h5" component="div" sx={{ fontWeight: 'bold' }}>
                FitnessApp
        </Typography>
            </Box>
          </Link>
            <Box sx={{ display: 'flex', gap: 2 }}>
              <Button component={Link} to="/bookedTrainings" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                All trainings
              </Button>
              <Button component={Link} to="/bookTrainingTrainer" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                Book training
              </Button>
              <Button component={Link} to="/cancelTrainingTrainer" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                Cancel training
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
                <Button component={Link} to="/registration" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  Sign In
                </Button>
                <Button component={Link} to="/login" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
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
        <Avatar sx={{ m: 1, bgcolor: '#283593' }}>
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

        <FormControl fullWidth margin="normal" required>
          <InputLabel id="training-type-label">Training Type</InputLabel>
          <Select
            labelId="training-type-label"
            value={trainingType}
            label="Training Type"
            onChange={(e) => setTrainingType(e.target.value)}
          >
            <MenuItem value="CARDIO">CARDIO</MenuItem>
            <MenuItem value="YOGA">YOGA</MenuItem>
            <MenuItem value="FUNCTIONAL">FUNCTIONAL</MenuItem>
            <MenuItem value="STRENGTH">STRENGTH</MenuItem>
          </Select>
        </FormControl>

        <TextField label="Cancel Deadline (hours)" type="number" value={cancelDeadline} onChange={(e) => setCancelDeadline(e.target.value)} fullWidth />


        <Button
          type="submit"
          fullWidth
          variant="contained"
          sx={{
            mt: 3,
            mb: 2,
            backgroundColor: '#4FC3F7',
            '&:hover': {
              backgroundColor: '#29B6F6', // slightly darker on hover
            },
          }}
        >
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
