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
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';


const defaultTheme = createTheme();

export default function BookedTrainings() {
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
    const [trainingDuration, setTrainingDuration] = useState(null);

  const [trainingType, setTrainingType] = useState('');
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

  const [trainingDate, setTrainingDate] = useState(null);
  const [trainings, setTrainings] = useState([]);
  const [selectedTrainingId, setSelectedTrainingId] = useState(null);
  const [bookingMessage, setBookingMessage] = useState('');
  const [viewType, setViewType] = useState('daily'); // 'daily' or 'weekly'

useEffect(() => {
  if (!trainingDate) return;

  const fetchTrainings = async () => {
    try {
      const formattedDate = trainingDate.toLocaleDateString('en-CA');
      const endpoint =
        viewType === 'daily'
          ? `http://localhost:8080/trainings/trainer/day?date=${formattedDate}&trainerUsername=${korisnicko_ime}`
          : `http://localhost:8080/trainings/trainer/week?startOfWeek=${formattedDate}&trainerUsername=${korisnicko_ime}`;

      const response = await axios.get(endpoint);
      setTrainings(response.data);
    } catch (error) {
      setTrainings([]);
      console.error('Failed to fetch trainings:', error);
    }
  };

  fetchTrainings();
}, [trainingDate, viewType, korisnicko_ime]);


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


function isValidTime(date) {
  return validTimes.some(t => {
    const validDate = parseTimeString(t);
    return validDate.getHours() === date.getHours() &&
           validDate.getMinutes() === date.getMinutes();
  });
}



  const handleOpenDialog2 = () => {
    setDialogMessage2("Feature Coming Soon...");
    setOpenDialog2(true);
  };

  const handleCloseDialog2 = () => {
    setOpenDialog2(false);
  };
  
  const handleCloseDialog = () => {
    setOpenDialog(false);
    navigate2('/login');
  };

  const logout = () => {
    localStorage.removeItem("jwtToken"); // Remove token

    // Redirect to login page
    window.location.href = "/login";  // or use `useNavigate` from React Router v6
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
      <Box sx={{ marginTop: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
          <FitnessCenterIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Available Trainings
        </Typography>
        <ToggleButtonGroup
        value={viewType}
        exclusive
        onChange={(event, newView) => {
            if (newView) {
            setViewType(newView);
            setSelectedTrainingId(null);
            setBookingMessage('');
            }
        }}
        sx={{ mt: 2 }}
        >
        <ToggleButton value="daily">Daily</ToggleButton>
        <ToggleButton value="weekly">Weekly</ToggleButton>
        </ToggleButtonGroup>
        <LocalizationProvider dateAdapter={AdapterDateFns}>
          <DatePicker
            label="Select a date"
            value={trainingDate}
            onChange={(newValue) => {
              setTrainingDate(newValue);
              setSelectedTrainingId(null);
              setBookingMessage('');
            }}
            renderInput={(params) => <TextField margin="normal" fullWidth {...params} />}
          />
        </LocalizationProvider>

        {trainings.length > 0 ? (
          <>
            <List sx={{ width: '100%', bgcolor: 'background.paper' }}>
              {trainings.map((training) => (
                <React.Fragment key={training.id}>
                  <ListItem
                    button
                    selected={selectedTrainingId === training.id}
                    onClick={() => navigate(`/trainingView/${training.id}`)}
                  >
                    <ListItemText
                      primary={`${toDateObject(training.startTime).toLocaleString('en-US', {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit',
                  })} - ${training.trainingType}`}
                      secondary={`Duration: ${training.duration} min, Trainer: ${training.trainer}`}
                    />
                  </ListItem>
                  <Divider />
                </React.Fragment>
              ))}
            </List>

            
             
          </>
        ) : trainingDate ? (
          <Typography sx={{ mt: 2 }}>No trainings available for selected day.</Typography>
        ) : null}

        {bookingMessage && (
          <Typography color="success.main" sx={{ mt: 2 }}>
            {bookingMessage}
          </Typography>
        )}
      </Box>
    </Container>



    </ThemeProvider>
    </div>
  );
}
